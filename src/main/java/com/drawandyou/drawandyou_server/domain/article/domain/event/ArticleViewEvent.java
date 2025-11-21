package com.drawandyou.drawandyou_server.domain.article.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ArticleViewEvent {
    private final Long articleId;
    private final Long userId;
    private final String ipAddress;
    private final LocalDateTime viewedAt;

    public static ArticleViewEvent of(Long articleId, Long userId, String ipAddress) {
        return new ArticleViewEvent(articleId, userId, ipAddress, LocalDateTime.now());
    }
}