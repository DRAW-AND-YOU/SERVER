package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response;

import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.DetailedScores;

public record DrawingAnalysisResponse(
        String colorAnalysis,
        String compositionAnalysis,
        String lineAnalysis,
        String emotionStatus,
        Integer totalScore,
        DetailedScores detailedScores
) {
}
