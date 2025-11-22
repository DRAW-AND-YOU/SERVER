package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * FastAPI 응답의 image_analysis 객체
 */
public record ImageAnalysisDto(
        @JsonProperty("total_score")
        Integer totalScore,

        @JsonProperty("object_score")
        Integer objectScore,

        @JsonProperty("image_score")
        Integer imageScore,

        @JsonProperty("question_score")
        Integer questionScore,

        @JsonProperty("analysis_result")
        String analysisResult
) {
}
