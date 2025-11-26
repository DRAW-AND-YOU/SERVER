package com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.dto.response;

import java.time.LocalDateTime;

public record TherapyProgramInfoResponse(
        LocalDateTime programStartDate,
        LocalDateTime programEndDate,
        Long therapyProgramId
) {
}
