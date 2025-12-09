package com.drawandyou.drawandyou_server.domain.dailycourse.presentation.dto;

public record DailyCourseScoreResponse(
        Long dailyCourseId,
        Long drawingAnalysisId,
        String imageUrl,
        String title,
        Integer round,
        Integer score
) {
}
