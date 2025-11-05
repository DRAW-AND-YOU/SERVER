package com.drawandyou.drawandyou_server.domain.drawinganalysis.application.service;

import com.drawandyou.drawandyou_server.domain.drawing.application.service.DrawingSaveService;
import com.drawandyou.drawandyou_server.domain.drawing.domain.entity.Drawing;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.entity.DrawingAnalysis;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.vo.MusicRecommendationValue;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.vo.PlaceRecommendationValue;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.vo.VideoRecommendationValue;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.DetailedScores;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.request.ContentRecommendRequest;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.request.DrawingAnalysisRequest;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.ContentRecommendationResponse;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.DrawingAnalysisAndRecommendationResponse;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.DrawingAnalysisResponse;
import com.drawandyou.drawandyou_server.domain.user.application.service.UserFindService;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import com.drawandyou.drawandyou_server.global.client.fastapi.FastApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DrawingAnalyzeService {

    private final DrawingSaveService drawingSaveService;
    private final DrawingAnalysisSaveService drawingAnalysisSaveService;
    private final UserFindService userFindService;

    private final FastApiClient fastApiClient;

    public DrawingAnalysisAndRecommendationResponse analyzeDrawingAndGetContentRecommendation(Long userId, DrawingAnalysisRequest drawingAnalysisRequest) {
        User user = userFindService.findUser(userId);
        String imageUrl = drawingAnalysisRequest.imageUrl();
        String title = drawingAnalysisRequest.title();

        Drawing drawing = Drawing.createDrawing(user, title, imageUrl);
        Drawing savedDrawing = drawingSaveService.save(drawing); // 그림 저장 완료

        // TODO: 실제로는 FastAPI 이미지 분석 비동기 호출
        // DrawingAnalysisResponse drawingAnalysisResponse = fastApiClient.getDrawingAnalysisResult(drawingSaveRequest, drawing.getImageUrl(), ...)

        // 더미 데이터로 DrawingAnalysisResponse 생성
        DrawingAnalysisResponse drawingAnalysisResponse = createDummyDrawingAnalysisResponse();

        // DrawingAnalysis 엔티티 생성
        DrawingAnalysis drawingAnalysis = DrawingAnalysis.createAnalysis(
                savedDrawing,
                drawingAnalysisResponse.colorAnalysis(),
                drawingAnalysisResponse.compositionAnalysis(),
                drawingAnalysisResponse.lineAnalysis(),
                drawingAnalysisResponse.emotionStatus(),
                drawingAnalysisResponse.totalScore(),
                drawingAnalysisResponse.detailedScores().objectScore(),
                drawingAnalysisResponse.detailedScores().imageScore(),
                drawingAnalysisResponse.detailedScores().questionScore()
        );

        // ContentRecommendRequest 생성 (drawingAnalysisResponse로부터 데이터 가져옴)
        ContentRecommendRequest contentRecommendRequest = new ContentRecommendRequest(
                drawingAnalysisResponse.totalScore(),
                drawingAnalysisResponse.detailedScores(),
                drawingAnalysisRequest.latitude(),   // 요청에서 받은 위도
                drawingAnalysisRequest.longitude()   // 요청에서 받은 경도
        );

        // fast api 컨텐츠 추천 api 호출
        ContentRecommendationResponse contentRecommendationResponse =
                fastApiClient.getContentRecommendationsSync(contentRecommendRequest);

        // 추천 결과를 Value Object로 변환
        List<MusicRecommendationValue> musicRecommendations = contentRecommendationResponse.music().stream()
                .map(m -> new MusicRecommendationValue(m.title(), m.artist(), m.url(), m.image()))
                .toList();

        List<VideoRecommendationValue> videoRecommendations = contentRecommendationResponse.video().stream()
                .map(v -> new VideoRecommendationValue(v.title(), v.url(), v.thumbnail()))
                .toList();

        List<PlaceRecommendationValue> placeRecommendations = contentRecommendationResponse.place().stream()
                .map(p -> new PlaceRecommendationValue(p.title(), p.address(), p.url(), p.rating(), p.image()))
                .toList();

        // DrawingAnalysis에 추천 결과 추가
        drawingAnalysis.addRecommendations(musicRecommendations, videoRecommendations, placeRecommendations);

        // 분석 결과 저장 (추천 결과 포함)
        drawingAnalysisSaveService.save(drawingAnalysis);

        // 최종 응답 생성
        return DrawingAnalysisAndRecommendationResponse.toResponse(
                title,
                imageUrl,
                drawingAnalysisResponse,
                contentRecommendationResponse
        );
    }

    /**
     * 더미 DrawingAnalysisResponse를 생성합니다.
     * TODO: 실제로는 FastAPI로부터 받아온 데이터를 사용해야 합니다.
     */
    private DrawingAnalysisResponse createDummyDrawingAnalysisResponse() {
        DetailedScores detailedScores = new DetailedScores(40, 40, 20);

        return new DrawingAnalysisResponse(
                "따뜻한 색상이 주를 이루며, 안정감을 주는 색상 조합입니다.",  // colorAnalysis
                "중앙에 주요 객체가 배치되어 균형잡힌 구도를 보여줍니다.",    // compositionAnalysis
                "부드러운 선이 특징적이며, 편안한 느낌을 전달합니다.",        // lineAnalysis
                "안정 / 행복",                                             // emotionStatus
                100,                                                      // totalScore
                detailedScores                                            // detailedScores
        );
    }

}
