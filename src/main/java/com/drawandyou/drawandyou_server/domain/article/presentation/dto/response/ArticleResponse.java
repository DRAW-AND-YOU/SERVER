package com.drawandyou.drawandyou_server.domain.article.presentation.dto.response;

import java.time.LocalDateTime;

public record ArticleResponse(
        Long articleId,
        String imageUrl,
        String title,
        String authorName,
        Long viewCount,
        Long likeCount,
        LocalDateTime createdAt
) {
}
