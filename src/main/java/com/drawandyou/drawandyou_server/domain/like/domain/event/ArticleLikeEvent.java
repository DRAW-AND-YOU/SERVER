package com.drawandyou.drawandyou_server.domain.like.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ArticleLikeEvent {
    private final Long articleId;
    private final Long userId;
    private final boolean liked; // true: 좋아요, false: 좋아요 취소
    private final LocalDateTime occurredAt;

    public static ArticleLikeEvent ofLike(Long articleId, Long userId) {
        return new ArticleLikeEvent(articleId, userId, true, LocalDateTime.now());
    }

    public static ArticleLikeEvent ofUnlike(Long articleId, Long userId) {
        return new ArticleLikeEvent(articleId, userId, false, LocalDateTime.now());
    }

    public boolean isLiked() {
        return liked;
    }
}