package com.drawandyou.drawandyou_server.domain.drawing.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record DrawingSaveRequest(

        @NotBlank(message = "이미지 URL 은 필수입니다.")
        @URL(message = "올바른 URL 형식이 아닙니다.")
        String imageUrl
) {
}
