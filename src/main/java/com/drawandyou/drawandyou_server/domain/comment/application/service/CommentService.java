package com.drawandyou.drawandyou_server.domain.comment.application.service;

import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleCommentCountRepository;
import com.drawandyou.drawandyou_server.domain.comment.domain.entity.ArticleCommentCount;
import com.drawandyou.drawandyou_server.domain.comment.domain.entity.Comment;
import com.drawandyou.drawandyou_server.domain.comment.domain.entity.CommentPath;
import com.drawandyou.drawandyou_server.domain.comment.domain.repository.CommentRepository;
import com.drawandyou.drawandyou_server.domain.comment.exception.CommentNotFoundException;
import com.drawandyou.drawandyou_server.domain.comment.exception.UnauthorizedCommentDeletionException;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.request.CommentCreateRequest;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.response.CommentCountResponse;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.response.CommentPageResponse;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.response.CommentResponse;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import com.drawandyou.drawandyou_server.domain.user.domain.repository.UserRepository;
import com.drawandyou.drawandyou_server.domain.user.exception.UserNotFoundException;
import com.drawandyou.drawandyou_server.global.common.application.service.PageLimitCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse create(Long userId, CommentCreateRequest request){

        Comment parent = findParent(request);
        CommentPath parentCommentPath = parent == null ? CommentPath.create("") : parent.getCommentPath();

        Comment comment = commentRepository.save(Comment.create(
                request.content(),
                request.articleId(),
                userId,
                parentCommentPath.createChildCommentPath(
                        commentRepository.findDescendantsTopPath(request.articleId(), parentCommentPath.getPath())
                                .orElse(null)
                ))
        );

        articleCommentCountRepository.increase(request.articleId());

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return CommentResponse.from(comment, user, userId);
    }

    private Comment findParent(CommentCreateRequest request) {

        String parentPath = request.parentPath();
        if (parentPath == null){
            return null;
        }
        // 상위 댓글을 찾고, 삭제도지 않은 댓글인지 확인
        return commentRepository.findByArticleIdAndPath(request.articleId(), parentPath)
                .filter(not(Comment::getDeleted))
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public CommentResponse read(Long commentId, Long currentUserId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        User user = userRepository.findById(comment.getUserId())
                .orElseThrow(UserNotFoundException::new);

        return CommentResponse.from(comment, user, currentUserId);
    }

    @Transactional
    public void delete(Long userId, Long commentId){

        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(CommentNotFoundException::new);

        // 댓글 작성자가 아닌 사용자가 삭제하려고 할때 예외 반환
        if (!comment.getUserId().equals(userId)){
            throw new UnauthorizedCommentDeletionException();
        }
        // 삭제한 댓글을 다시 삭제하려고 하면, early return
        if (comment.getDeleted()){
            return;
        }

        if (hasChildren(comment)){
            comment.delete(); // 자식이 있다면, 삭제 표시만
        } else {
            delete(comment); // 자식이 존재하지 않으면, 실제로 db 에서 삭제
        }

    }

    private boolean hasChildren(Comment comment){
        return commentRepository.findDescendantsTopPath(
                comment.getArticleId(),
                comment.getCommentPath().getPath()
        ).isPresent();
    }

    private void delete(Comment comment){
        commentRepository.delete(comment); // db 에서 삭제
        articleCommentCountRepository.decrease(comment.getArticleId());
        // 부모 검사
        if (!comment.isRoot()){
            commentRepository.findByArticleIdAndPath(comment.getArticleId(), comment.getCommentPath().getParentPath())
                    .filter(Comment::getDeleted) // 부모 댓글이 삭제 표시 상태인지 확인
                    .filter(not(this::hasChildren)) // 삭제 표시상태일때, 자식이 없는지 확인
                    .ifPresent(this::delete); // 그렇다면, 부모댓글 재귀적으로 삭제
        }

    }

    public CommentPageResponse readAll(Long articleId, Long page, Long pageSize, Long currentUserId){
        Long offset = page * pageSize;
        Long limit = PageLimitCalculator.calculatePageLimit(page, pageSize, 10L);

        // 댓글 목록 조회
        List<Comment> commentList = commentRepository.findAll(articleId, offset, pageSize);

        // 댓글 응답 DTO 변환
        List<CommentResponse> commentResponses = convertToResponses(commentList, currentUserId);

        // 전체 댓글 수 조회
        long totalElements = commentRepository.count(articleId, limit);

        return CommentPageResponse.of(
                commentResponses,
                page,
                pageSize.intValue(),
                totalElements
        );
    }

    private List<CommentResponse> convertToResponses(List<Comment> comments, Long currentUserId) {
        if (comments.isEmpty()) {
            return List.of();
        }

        List<Long> userIds = comments.stream()
                .map(Comment::getUserId)
                .distinct()
                .toList();

        List<User> users = userRepository.findAllById(userIds);

        // userId를 키로 하는 Map 생성
        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        // CommentResponse 변환
        return comments.stream()
                .map(comment -> {
                    User user = userMap.get(comment.getUserId());
                    return CommentResponse.from(comment, user, currentUserId);
                })
                .toList();
    }

    public CommentCountResponse count(Long articleId){
        Long count = articleCommentCountRepository.findById(articleId)
                .map(ArticleCommentCount::getCommentCount)
                .orElse(0L);

        return new CommentCountResponse(count);
    }


}
