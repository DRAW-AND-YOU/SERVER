package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response;

import java.util.List;

/**
 * FastAPI 응답의 content_recommendation 객체
 */
public record ContentRecommendationDto(
        List<MusicRecommendation> music,
        List<VideoRecommendation> video,
        List<PlaceRecommendation> place
) {
}
