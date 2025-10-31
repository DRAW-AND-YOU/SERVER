package com.drawandyou.drawandyou_server.domain.user.presentation.dto.request;

import com.drawandyou.drawandyou_server.domain.user.domain.entity.enums.Gender;

import java.time.LocalDate;
import java.util.List;

public record RegisterRequest(
        String email,
        String password,
        String username,
        String nickname,
        LocalDate birthDate,
        Gender gender,
        List<String> hobbies
) {
}
