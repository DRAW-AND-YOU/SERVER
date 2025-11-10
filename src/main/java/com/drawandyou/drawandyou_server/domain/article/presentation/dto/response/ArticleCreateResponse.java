package com.drawandyou.drawandyou_server.domain.article.presentation.dto.response;

public record ArticleCreateResponse(

        Long userId,
        Long articleId,
        String imageUrl
) {

    public static ArticleCreateResponse toResponse(Long userId, Long articleId, String articleImageUrl) {
        return new ArticleCreateResponse(
                userId,
                articleId,
                articleImageUrl
        );
    }
}
