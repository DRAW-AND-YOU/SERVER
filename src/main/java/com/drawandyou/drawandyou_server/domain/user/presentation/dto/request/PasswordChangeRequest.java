package com.drawandyou.drawandyou_server.domain.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequest(
        @NotBlank(message = "현재 비밀번호는 필수입니다.")
        String currentPassword,
        @NotBlank(message = "변경할 비밀번호는 필수입니다.")
        String newPassword
) {
}
