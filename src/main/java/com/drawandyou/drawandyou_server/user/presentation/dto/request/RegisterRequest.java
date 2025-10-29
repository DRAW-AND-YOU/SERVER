package com.drawandyou.drawandyou_server.user.presentation.dto.request;

public record RegisterRequest(
        String username,
        String password
) {
}
