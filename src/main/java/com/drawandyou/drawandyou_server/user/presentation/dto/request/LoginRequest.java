package com.drawandyou.drawandyou_server.user.presentation.dto.request;

public record LoginRequest(
        String username,
        String password
) {
}
