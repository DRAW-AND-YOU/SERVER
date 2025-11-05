package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.request;

import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.DetailedScores;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ContentRecommendRequest(

        @NotNull(message = "점수는 필수입니다.")
        Integer score,

        @NotNull(message = "세부 점수는 필수입니다.")
        @JsonProperty("detailed_scores")
        DetailedScores detailedScores,

        @NotNull(message = "사용자의 위도 값은 필수입니다.")
        Double latitude,

        @NotNull(message = "사용자의 경도 값은 필수입니다.")
        Double longitude

) {
}
