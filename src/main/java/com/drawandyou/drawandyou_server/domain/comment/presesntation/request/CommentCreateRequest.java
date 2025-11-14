package com.drawandyou.drawandyou_server.domain.comment.presesntation.request;

public record CommentCreateRequest(
        Long articleId,
        String content,
        String parentPath,
        Long userId
) {
}
