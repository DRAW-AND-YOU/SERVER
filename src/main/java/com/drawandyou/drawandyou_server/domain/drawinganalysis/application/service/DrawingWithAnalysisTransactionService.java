package com.drawandyou.drawandyou_server.domain.drawinganalysis.application.service;

import com.drawandyou.drawandyou_server.domain.drawing.application.service.DrawingSaveService;
import com.drawandyou.drawandyou_server.domain.drawing.domain.entity.Drawing;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.entity.DrawingAnalysis;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.vo.MusicRecommendationValue;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.vo.PlaceRecommendationValue;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.vo.VideoRecommendationValue;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.FastApiRecommendResponse;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.ImageAnalysisDto;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.ContentRecommendationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Drawing과 DrawingAnalysis를 트랜잭션 내에서 함께 저장하는 서비스
 */
@Service
@RequiredArgsConstructor
public class DrawingWithAnalysisTransactionService {

    private final DrawingSaveService drawingSaveService;
    private final DrawingAnalysisSaveService drawingAnalysisSaveService;

    @Transactional
    public DrawingAnalysis saveDrawingWithAnalysis(Drawing drawing,
            FastApiRecommendResponse fastApiResponse) {

        // Drawing 저장
        Drawing savedDrawing = drawingSaveService.save(drawing);

        // image_analysis 데이터 추출
        ImageAnalysisDto imageAnalysis = fastApiResponse.imageAnalysis();

        // DrawingAnalysis 엔티티 생성
        DrawingAnalysis drawingAnalysis = DrawingAnalysis.createAnalysis(
                savedDrawing,
                imageAnalysis.totalScore(),
                imageAnalysis.objectScore(),
                imageAnalysis.imageScore(),
                imageAnalysis.questionScore(),
                imageAnalysis.analysisResult()
        );

        // 추천 결과 할당
        ContentRecommendationDto contentRecommendation = fastApiResponse.contentRecommendation();
        assignRecommendationResultsToEntity(contentRecommendation, drawingAnalysis);

        // DrawingAnalysis 저장
        return drawingAnalysisSaveService.save(drawingAnalysis);
    }

    private void assignRecommendationResultsToEntity(
            ContentRecommendationDto contentRecommendation,
            DrawingAnalysis drawingAnalysis) {

        // 추천 결과를 Value Object로 변환
        List<MusicRecommendationValue> musicRecommendations = Optional.ofNullable(contentRecommendation.music())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(m -> new MusicRecommendationValue(m.title(), m.artist(), m.url(), m.image()))
                .toList();

        List<VideoRecommendationValue> videoRecommendations = Optional.ofNullable(contentRecommendation.video())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(v -> new VideoRecommendationValue(v.title(), v.url(), v.thumbnail()))
                .toList();

        List<PlaceRecommendationValue> placeRecommendations = Optional.ofNullable(contentRecommendation.place())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(p -> new PlaceRecommendationValue(p.title(), p.address(), p.url(), p.rating(), p.image()))
                .toList();

        // DrawingAnalysis에 추천 결과 추가
        drawingAnalysis.addRecommendations(musicRecommendations, videoRecommendations, placeRecommendations);
    }
}
