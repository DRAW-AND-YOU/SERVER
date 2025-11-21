package com.drawandyou.drawandyou_server.domain.article.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ArticleViewEvent {
    private final Long articleId;
    private final Long userId;
    private final LocalDateTime viewedAt;

    public static ArticleViewEvent of(Long articleId, Long userId) {
        return new ArticleViewEvent(articleId, userId,LocalDateTime.now());
    }
}