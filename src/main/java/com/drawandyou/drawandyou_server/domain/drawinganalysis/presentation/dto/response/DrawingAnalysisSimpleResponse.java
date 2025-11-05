package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response;

import java.time.LocalDateTime;

public record DrawingAnalysisSimpleResponse(
        Long drawingAnalysisId,
        String imageUrl,
        String title,
        LocalDateTime drawnAt
) {
}
