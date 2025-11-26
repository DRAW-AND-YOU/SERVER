package com.drawandyou.drawandyou_server.domain.dailycourse.presentation.dto;

public record DailyCourseScoreResponse(
        Long dailyCourseId,
        String imageUrl,
        Integer round,
        Integer score
) {
}
