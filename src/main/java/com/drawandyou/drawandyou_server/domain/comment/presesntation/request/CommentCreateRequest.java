package com.drawandyou.drawandyou_server.domain.comment.presesntation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest(
        @NotNull(message = "게시글 id 는 필수입니다.")
        Long articleId,
        @NotBlank(message = "댓글 내용은 필수입니다.")
        String content,
        String parentPath
) {
}
