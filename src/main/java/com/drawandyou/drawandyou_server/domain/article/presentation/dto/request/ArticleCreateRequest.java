package com.drawandyou.drawandyou_server.domain.article.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ArticleCreateRequest(
        @NotBlank(message = "게시글의 제목은 비어있을 수 없습니다.")
        String title,
        @NotBlank(message = "게시글의 본문 내용은 비어있을 수 없습니다.")
        String content,
        @NotBlank(message = "게시글의 이미지는 필수적으로 포함되어야 합니다.")
        String imageUrl
) {
}
