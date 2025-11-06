package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response;

import java.util.List;

public record DrawingAnalysisAndRecommendationResponse(
        // 그림 정보
        Long drawingId,
        String title,
        String imageUrl,

        // 심리 분석 결과
        Integer totalScore,
        String colorAnalysis,
        String compositionAnalysis,
        String lineAnalysis,
        String emotionStatus,

        // AI 추천 결과
        List<MusicRecommendation> musicRecommendations,
        List<VideoRecommendation> videoRecommendations,
        List<PlaceRecommendation> placeRecommendations
) {

    /**
     * DrawingAnalysisResponse와 ContentRecommendationResponse를 합쳐서 최종 응답을 생성합니다.
     */
    public static DrawingAnalysisAndRecommendationResponse toResponse(
            Long drawingId,
            String drawingTitle,
            String drawingImageUrl,
            DrawingAnalysisResponse analysisResponse,
            ContentRecommendationResponse recommendationResponse
    ) {
        return new DrawingAnalysisAndRecommendationResponse(
                drawingId,
                drawingTitle,
                drawingImageUrl,
                analysisResponse.totalScore(),
                analysisResponse.colorAnalysis(),
                analysisResponse.compositionAnalysis(),
                analysisResponse.lineAnalysis(),
                analysisResponse.emotionStatus(),
                recommendationResponse.music(),
                recommendationResponse.video(),
                recommendationResponse.place()
        );
    }
}
