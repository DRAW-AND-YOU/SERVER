package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response;

import com.drawandyou.drawandyou_server.domain.drawing.domain.entity.Drawing;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.entity.DrawingAnalysis;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.vo.MusicRecommendationValue;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.vo.PlaceRecommendationValue;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.vo.VideoRecommendationValue;

import java.util.List;

public record DrawingAnalysisDetailResponse(
        Long drawingAnalysisId,
        // 그림 정보
        String title,
        String imageUrl,

        // 심리 분석 결과
        Integer totalScore,
        String analysisResult,

        // AI 추천 결과
        List<MusicRecommendationValue> musicRecommendations,
        List<VideoRecommendationValue> videoRecommendations,
        List<PlaceRecommendationValue> placeRecommendations
) {
    public static DrawingAnalysisDetailResponse toResponse(DrawingAnalysis drawingAnalysis) {

        Drawing drawing = drawingAnalysis.getDrawing();

        return new DrawingAnalysisDetailResponse(
                drawingAnalysis.getId(),
                drawing.getTitle(),
                drawing.getImageUrl(),
                drawingAnalysis.getTotalScore(),
                drawingAnalysis.getAnalysisResult(),
                drawingAnalysis.getMusicRecommendations(),
                drawingAnalysis.getVideoRecommendations(),
                drawingAnalysis.getPlaceRecommendations()
        );
    }
}
