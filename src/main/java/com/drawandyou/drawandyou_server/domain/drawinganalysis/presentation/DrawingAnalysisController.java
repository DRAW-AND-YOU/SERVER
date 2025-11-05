package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation;

import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.request.DrawingAnalysisRequest;
import com.drawandyou.drawandyou_server.domain.drawing.presentation.message.ResponseMessage;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.application.service.DrawingAnalyzeService;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.DrawingAnalysisAndRecommendationResponse;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "DRAWING ANALYSIS", description = "그림 분석 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drawing-analysis")
public class DrawingAnalysisController {

    private final DrawingAnalyzeService drawingAnalyzeService;

    @Operation(summary = "그림 분석결과 및 컨텐츠 추천결과 반환", description = "사용자가 그린 그림을 데이터 베이스에 저장하고 Fast API로부터 AI 분석 결과를 반환하는 API 입니다.")
    @PostMapping
    public ApiResponse<DrawingAnalysisAndRecommendationResponse> analyzeDrawingAndGetContentRecommendation(@AuthenticationPrincipal Long userId,
                                                                                                            @RequestBody DrawingAnalysisRequest drawingSaveRequest){
        DrawingAnalysisAndRecommendationResponse response = drawingAnalyzeService.analyzeDrawingAndGetContentRecommendation(userId, drawingSaveRequest);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.DRAWING_ANALYSIS_SUCCESS.getMessage(), response);
    }
}
