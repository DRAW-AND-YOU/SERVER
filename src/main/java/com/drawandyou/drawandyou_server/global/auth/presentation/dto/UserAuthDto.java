package com.drawandyou.drawandyou_server.global.auth.presentation.dto;

import lombok.Builder;

@Builder
public record UserAuthDto(
        String token,
        String username,
        String password,
        Long id
) {
}
