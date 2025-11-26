package com.drawandyou.drawandyou_server.domain.dailycourse.presentation.dto;

public record DailyCourseScoreResponse(
        Long dailyCourseId,
        Integer round,
        Integer score
) {
}
