package com.drawandyou.drawandyou_server.domain.comment.presesntation.response;

import com.drawandyou.drawandyou_server.domain.comment.domain.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long commentId,
        String content,
        Long articleId,
        Long userId,
        Boolean deleted,
        String path,
        LocalDateTime createdAt
) {

    public static CommentResponse from(Comment comment){
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getArticleId(),
                comment.getUserId(),
                comment.getDeleted(),
                comment.getCommentPath().getPath(),
                comment.getCreatedAt()
        );
    }
}
