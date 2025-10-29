package com.drawandyou.drawandyou_server.domain.user.presentation.dto.response;

import lombok.Builder;

@Builder
public record CurrentLoginUserResponse(
        Long userId,
        String username
) {
}
