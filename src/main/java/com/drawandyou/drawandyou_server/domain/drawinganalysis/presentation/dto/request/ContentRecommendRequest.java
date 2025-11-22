package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record ContentRecommendRequest(

        @JsonProperty("image_url")
        String imageUrl,
        @JsonProperty("image_type")
        Integer imageType,
        @JsonProperty("question_responses")
        List<String> questionResponses,
        Double latitude,
        Double longitude
) {
    public static ContentRecommendRequest from(DrawingAnalysisRequest request){
        return new ContentRecommendRequest(
                request.imageUrl(),
                request.imageType().getNumber(),
                request.followUpQuestions(),
                request.latitude(),
                request.longitude()
        );
    }
}
