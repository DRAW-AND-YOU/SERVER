package com.drawandyou.drawandyou_server.domain.article.application.service.dto;

public record ViewResult(
        Long count,
        boolean incremented
) {
}
