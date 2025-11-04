package com.drawandyou.drawandyou_server.domain.user.presentation.dto.request;

import com.drawandyou.drawandyou_server.domain.user.domain.entity.enums.Gender;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.enums.Hobby;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.List;

public record ExtraRegisterRequest(
        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname,
        @NotNull(message = "생년월일은 필수입니다.")
        @Past(message = "생년월일은 과거 날짜여야 합니다.")
        LocalDate birthDate,
        @NotNull(message = "성별은 필수입니다.")
        Gender gender,
        List<Hobby> hobbies
) {
}
