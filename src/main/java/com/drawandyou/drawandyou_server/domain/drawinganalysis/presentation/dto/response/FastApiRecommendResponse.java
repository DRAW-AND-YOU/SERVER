package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * FastAPI /recommend 엔드포인트의 전체 응답 구조
 */
public record FastApiRecommendResponse(
        @JsonProperty("image_analysis")
        ImageAnalysisDto imageAnalysis,

        @JsonProperty("content_recommendation")
        ContentRecommendationDto contentRecommendation
) {
}