package com.drawandyou.drawandyou_server.global.common.response;

import java.time.LocalDateTime;

public record ScrollInfo(
        LocalDateTime lastCreatedAt,
        Long lastArticleId,
        boolean hasNext,
        int size
) {
    public static ScrollInfo empty() {
        return new ScrollInfo(null, null, false, 0);
    }

    public static ScrollInfo of(LocalDateTime lastCreatedAt, Long lastArticleId, boolean hasNext, int size) {
        return new ScrollInfo(lastCreatedAt, lastArticleId, hasNext, size);
    }
}
