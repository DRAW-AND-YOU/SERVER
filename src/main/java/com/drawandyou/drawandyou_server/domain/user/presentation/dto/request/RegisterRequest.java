package com.drawandyou.drawandyou_server.domain.user.presentation.dto.request;

public record RegisterRequest(
        String username,
        String password
) {
}
