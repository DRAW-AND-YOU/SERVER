package com.drawandyou.drawandyou_server.domain.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record ProfileImageChangeRequest(
        @URL(message = "올바르지 않은 URL 형식입니다.")
        @NotBlank(message = "프로필 이미지 URL 은 필수입니다.")
        String profileImageUrl
) {

}
