package com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.dto.response;

import java.util.List;

public record 
ParticipatedProgramIdResponse(
        List<TherapyProgramInfoResponse> therapyProgramInfos
) {
}
