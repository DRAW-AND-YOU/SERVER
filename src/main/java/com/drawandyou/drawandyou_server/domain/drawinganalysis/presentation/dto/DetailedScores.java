package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record DetailedScores(
        @NotNull(message = "객체 점수는 필수입니다.")
        @JsonProperty("object_score")
        Integer objectScore,

        @NotNull(message = "이미지 점수는 필수입니다.")
        @JsonProperty("image_score")
        Integer imageScore,

        @NotNull(message = "질문 점수는 필수입니다.")
        @JsonProperty("question_score")
        Integer questionScore
) {
}
