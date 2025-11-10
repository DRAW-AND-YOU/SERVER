package com.drawandyou.drawandyou_server.domain.article.presentation.dto.request;


public record ArticleUpdateRequest(
        String title,
        String content,
        String imageUrl
) {
}