package com.drawandyou.drawandyou_server.domain.article.presentation.dto.response;

import java.time.LocalDateTime;

public record PopularArticleResponse(
        Long articleId,
        String articleImageUrl,
        String title,
        String authorName,
        String authorImageUrl,
        Long viewCount,
        Long likeCount,
        Long commentCount,
        LocalDateTime createdAt
) {
}
