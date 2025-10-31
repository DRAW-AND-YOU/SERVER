package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response;

public record DrawingAnalysisResponse(
        String analysisResult
        // json 응답은, fast api 응답에 맞게 추후 수정해야한다.
) {
}
