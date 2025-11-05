package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response;

import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.DetailedScores;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ContentRecommendationResponse(
        Integer score,

        @JsonProperty("detailed_scores")
        DetailedScores detailedScores,

        String emotion,

        List<MusicRecommendation> music,

        List<VideoRecommendation> video,

        List<PlaceRecommendation> place
) {
}