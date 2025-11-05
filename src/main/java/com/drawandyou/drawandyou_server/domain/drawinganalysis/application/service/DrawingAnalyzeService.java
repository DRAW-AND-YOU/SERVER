package com.drawandyou.drawandyou_server.domain.drawinganalysis.application.service;

import com.drawandyou.drawandyou_server.domain.drawing.domain.entity.Drawing;
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

@Service
@RequiredArgsConstructor
public class DrawingAnalyzeService {

    private final UserFindService userFindService;
    private final FastApiClient fastApiClient;
    private final DrawingWithAnalysisTransactionService transactionService;

    /**
     * 그림 분석 및 컨텐츠 추천을 수행합니다.
     *
     * 트랜잭션 전략:
     * 1. 외부 API 호출 (FastAPI)을 트랜잭션 밖에서 먼저 수행
     * 2. 모든 외부 호출이 성공한 후, 별도 트랜잭션 서비스를 통해 Drawing + DrawingAnalysis 저장
     * 3. 이를 통해 DB 커넥션 효율과 데이터 일관성을 모두 확보
     */
    public DrawingAnalysisAndRecommendationResponse analyzeDrawingAndGetContentRecommendation(
            Long userId,
            DrawingAnalysisRequest drawingAnalysisRequest) {

        User user = userFindService.findUser(userId);
        String imageUrl = drawingAnalysisRequest.imageUrl();
        String title = drawingAnalysisRequest.title();

        // 1. Drawing 엔티티 생성 (메모리만, 저장 X)
        Drawing drawing = Drawing.createDrawing(user, title, imageUrl);

        // 2. 외부 API 호출 (트랜잭션 밖에서 실행)
        // TODO: 실제로는 FastAPI 이미지 분석 비동기 호출
        DrawingAnalysisResponse drawingAnalysisResponse = createDummyDrawingAnalysisResponse();

        ContentRecommendRequest contentRecommendRequest = new ContentRecommendRequest(
                drawingAnalysisResponse.totalScore(),
                drawingAnalysisResponse.detailedScores(),
                drawingAnalysisRequest.latitude(),
                drawingAnalysisRequest.longitude()
        );

        ContentRecommendationResponse contentRecommendationResponse =
                fastApiClient.getContentRecommendationsSync(contentRecommendRequest);

        // 3. 외부 API 호출 성공 후, 트랜잭션 서비스를 통해 모든 엔티티 저장
        transactionService.saveDrawingWithAnalysis(drawing, drawingAnalysisResponse, contentRecommendationResponse);

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
