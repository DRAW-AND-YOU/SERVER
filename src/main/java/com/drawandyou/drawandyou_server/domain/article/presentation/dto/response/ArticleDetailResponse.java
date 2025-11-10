package com.drawandyou.drawandyou_server.domain.article.presentation.dto.response;

import com.drawandyou.drawandyou_server.domain.article.domain.entity.Article;
import com.drawandyou.drawandyou_server.domain.articleimage.domain.entity.ArticleImage;

import java.time.LocalDateTime;

public record ArticleDetailResponse(
        Long articleId,
        Long userId,
        String username,
        String title,
        String content,
        String imageUrl,
        LocalDateTime createdAt
) {
    public static ArticleDetailResponse toResponse(
          Article article, ArticleImage articleImage
    ) {
        return new ArticleDetailResponse(
                article.getId(),
                article.getUser().getId(),
                article.getUser().getUsername(),
                article.getTitle(),
                article.getContent(),
                articleImage.getImageUrl(),
                article.getCreatedAt()
        );
    }
}