package com.drawandyou.drawandyou_server.domain.user.presentation.dto.request;

public record LoginRequest(
        String username,
        String password
) {
}
