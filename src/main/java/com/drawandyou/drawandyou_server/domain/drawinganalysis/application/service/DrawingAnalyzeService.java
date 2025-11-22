package com.drawandyou.drawandyou_server.domain.drawinganalysis.application.service;

import com.drawandyou.drawandyou_server.domain.drawing.domain.entity.Drawing;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.entity.DrawingAnalysis;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.request.ContentRecommendRequest;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.request.DrawingAnalysisRequest;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.FastApiRecommendResponse;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.DrawingAnalysisAndRecommendationResponse;
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

        ContentRecommendRequest contentRecommendRequest = ContentRecommendRequest.from(drawingAnalysisRequest);

        FastApiRecommendResponse fastApiResponse =
                fastApiClient.getContentRecommendationsSync(contentRecommendRequest);

        // 3. 외부 API 호출 성공 후, 트랜잭션 서비스를 통해 모든 엔티티 저장
        DrawingAnalysis drawingAnalysis = transactionService.saveDrawingWithAnalysis(drawing, fastApiResponse);

        // 최종 응답 생성
        return DrawingAnalysisAndRecommendationResponse.toResponse(
                drawingAnalysis.getDrawing().getId(),
                title,
                imageUrl,
                fastApiResponse
        );
    }
}
