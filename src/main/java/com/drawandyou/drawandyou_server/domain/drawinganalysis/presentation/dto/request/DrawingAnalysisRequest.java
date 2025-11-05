package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record DrawingAnalysisRequest(

        @NotBlank(message = "이미지 URL 은 필수입니다.")
        @URL(message = "올바른 URL 형식이 아닙니다.")
        String imageUrl,

        @NotBlank(message = "그림의 제목은 필수입니다.")
        String title,

        @NotNull(message = "사용자의 위도 값은 필수입니다.")
        Double latitude,

        @NotNull(message = "사용자의 경도 값은 필수입니다.")
        Double longitude,

        @NotEmpty(message = "후질문 정보는 최소 1개 이상이어야 합니다.")
        @Valid
        List<FollowUpQuestion> followUpQuestions
) {

    /**
     * 후질문과 사용자의 답변을 담는 레코드
     */
    public record FollowUpQuestion(
            @NotBlank(message = "질문 내용은 필수입니다.")
            String question,

            @NotBlank(message = "답변 내용은 필수입니다.")
            String answer
    ) {}
}
