package com.drawandyou.drawandyou_server.domain.comment.application.service;

import com.drawandyou.drawandyou_server.domain.comment.domain.entity.Comment;
import com.drawandyou.drawandyou_server.domain.comment.domain.entity.CommentPath;
import com.drawandyou.drawandyou_server.domain.comment.domain.repository.CommentRepository;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.request.CommentCreateRequest;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.response.CommentPageResponse;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.response.CommentResponse;
import com.drawandyou.drawandyou_server.global.common.application.service.PageLimitCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse create(CommentCreateRequest request){
        Comment parent = findParent(request);
        CommentPath parentCommentPath = parent == null ? CommentPath.create("") : parent.getCommentPath();
        Comment comment = commentRepository.save(Comment.create(
                request.content(),
                request.articleId(),
                request.userId(),
                parentCommentPath.createChildCommentPath(
                        commentRepository.findDescendantsTopPath(request.articleId(), parentCommentPath.getPath())
                                .orElse(null)
                ))
        );
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

    public CommentResponse read(Long commentId){
        return CommentResponse.from(
                commentRepository.findById(commentId)
                        .orElseThrow()
        );
    }

    @Transactional
    public void delete(Long commentId){
        commentRepository.findById(commentId)
                .filter(not(Comment::getDeleted))
                .ifPresent(comment -> {
                    if (hasChildren(comment)){
                        comment.delete(); // 자식이 있다면 삭제 표시만
                    } else{
                        delete(comment); // 자식이 없다면 실제로 삭제
                    }
                });
    }

    private boolean hasChildren(Comment comment){
        return commentRepository.findDescendantsTopPath(
                comment.getArticleId(),
                comment.getCommentPath().getPath()
        ).isPresent();
    }

    private void delete(Comment comment){
        commentRepository.delete(comment);
        if (!comment.isRoot()){
            commentRepository.findByPath(comment.getCommentPath().getParentPath())
                    .filter(Comment::getDeleted)
                    .filter(not(this::hasChildren))
                    .ifPresent(this::delete);
        }

    }

    public CommentPageResponse readAll(Long articleId, Long page, Long pageSize){
        return CommentPageResponse.of(
                commentRepository.findAll(articleId, (page-1) * pageSize, pageSize).stream()
                        .map(CommentResponse::from)
                        .toList(),
                commentRepository.count(articleId, PageLimitCalculator.calculatePageLimit(page, pageSize, 10L))
        );
    }


}
