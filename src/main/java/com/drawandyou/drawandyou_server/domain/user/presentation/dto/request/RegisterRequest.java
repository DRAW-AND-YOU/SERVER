package com.drawandyou.drawandyou_server.domain.user.presentation.dto.request;

import com.drawandyou.drawandyou_server.domain.user.domain.entity.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.List;

public record RegisterRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,
        @NotBlank(message = "사용자명은 필수입니다.")
        String username,
        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname,
        @NotNull(message = "생년월일은 필수입니다.")
        @Past(message = "생년월일은 과거 날짜여야 합니다.")
        LocalDate birthDate,
        @NotNull(message = "성별은 필수입니다.")
        Gender gender,
        List<String> hobbies
) {
}
