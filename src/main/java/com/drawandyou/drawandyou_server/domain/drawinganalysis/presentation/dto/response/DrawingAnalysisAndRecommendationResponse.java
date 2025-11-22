package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response;

import java.util.List;

public record DrawingAnalysisAndRecommendationResponse(
        // 그림 정보
        Long drawingId,
        String title,
        String imageUrl,

        // 심리 분석 결과
        Integer totalScore,
        Integer objectScore,
        Integer imageScore,
        Integer questionScore,
        String analysisResult,

        // AI 추천 결과
        List<MusicRecommendation> musicRecommendations,
        List<VideoRecommendation> videoRecommendations,
        List<PlaceRecommendation> placeRecommendations
) {

    /**
     * FastApiRecommendResponse를 사용하여 최종 응답을 생성합니다.
     */
    public static DrawingAnalysisAndRecommendationResponse toResponse(
            Long drawingId,
            String drawingTitle,
            String drawingImageUrl,
            FastApiRecommendResponse fastApiResponse
    ) {
        ImageAnalysisDto imageAnalysis = fastApiResponse.imageAnalysis();
        ContentRecommendationDto contentRecommendation = fastApiResponse.contentRecommendation();

        return new DrawingAnalysisAndRecommendationResponse(
                drawingId,
                drawingTitle,
                drawingImageUrl,
                imageAnalysis.totalScore(),
                imageAnalysis.objectScore(),
                imageAnalysis.imageScore(),
                imageAnalysis.questionScore(),
                imageAnalysis.analysisResult(),
                contentRecommendation.music(),
                contentRecommendation.video(),
                contentRecommendation.place()
        );
    }
}
