package com.drawandyou.drawandyou_server.domain.comment.presesntation.response;

import com.drawandyou.drawandyou_server.domain.comment.domain.entity.Comment;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;

import java.time.LocalDateTime;

public record CommentResponse(
        Long commentId,
        String content,
        Long articleId,
        Long userId,
        String userName,
        String userProfileUrl,
        Boolean deleted,
        String path,
        Integer depth,
        Boolean isMine,
        LocalDateTime createdAt
) {

    public static CommentResponse from(Comment comment, User user, Long currentUserId){
        boolean isMine = comment.getUserId().equals(currentUserId);

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getArticleId(),
                comment.getUserId(),
                user.getNickname(),
                user.getProfileImageUrl(),
                comment.getDeleted(),
                comment.getCommentPath().getPath(),
                comment.getCommentPath().getDepth(),
                isMine,
                comment.getCreatedAt()
        );
    }
}
