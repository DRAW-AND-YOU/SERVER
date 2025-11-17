package com.drawandyou.drawandyou_server.domain.comment.application.service;

import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleCommentCountRepository;
import com.drawandyou.drawandyou_server.domain.comment.domain.entity.ArticleCommentCount;
import com.drawandyou.drawandyou_server.domain.comment.domain.entity.Comment;
import com.drawandyou.drawandyou_server.domain.comment.domain.entity.CommentPath;
import com.drawandyou.drawandyou_server.domain.comment.domain.repository.CommentRepository;
import com.drawandyou.drawandyou_server.domain.comment.exception.CommentNotFoundException;
import com.drawandyou.drawandyou_server.domain.comment.exception.UnauthorizedCommentDeletionException;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.request.CommentCreateRequest;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.response.CommentPageResponse;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.response.CommentResponse;
import com.drawandyou.drawandyou_server.global.common.application.service.PageLimitCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;

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

        return CommentResponse.from(comment);
    }

    private Comment findParent(CommentCreateRequest request) {

        String parentPath = request.parentPath();
        if (parentPath == null){
            return null;
        }
        // 상위 댓글을 찾고, 삭제도지 않은 댓글인지 확인
        return commentRepository.findByPath(parentPath)
                .filter(not(Comment::getDeleted))
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public CommentResponse read(Long commentId){
        return CommentResponse.from(
                commentRepository.findById(commentId)
                        .orElseThrow()
        );
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
            commentRepository.findByPath(comment.getCommentPath().getParentPath())
                    .filter(Comment::getDeleted) // 부모 댓글이 삭제 표시 상태인지 확인
                    .filter(not(this::hasChildren)) // 삭제 표시상태일때, 자식이 없는지 확인
                    .ifPresent(this::delete); // 그렇다면, 부모댓글 재귀적으로 삭제
        }

    }

    public CommentPageResponse readAll(Long articleId, Long page, Long pageSize){
        Long offset = (page - 1) * pageSize;
        Long limit = PageLimitCalculator.calculatePageLimit(page, pageSize, 10L);

        List<CommentResponse> comments = commentRepository.findAll(articleId, offset, pageSize).stream()
                .map(CommentResponse::from)
                .toList();

        long totalElements = commentRepository.count(articleId, limit);

        return CommentPageResponse.of(
                comments,
                page,
                pageSize.intValue(),
                totalElements
        );
    }

    public Long count(Long articleId){
        return articleCommentCountRepository.findById(articleId)
                .map(ArticleCommentCount::getCommentCount)
                .orElse(0L);
    }


}
