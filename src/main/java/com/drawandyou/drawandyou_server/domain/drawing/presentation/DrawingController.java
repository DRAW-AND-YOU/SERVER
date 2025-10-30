package com.drawandyou.drawandyou_server.domain.drawing.presentation;

import com.drawandyou.drawandyou_server.domain.drawing.presentation.dto.response.DrawingAnalysisResponse;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import com.drawandyou.drawandyou_server.domain.drawing.application.service.DrawingSaveService;
import com.drawandyou.drawandyou_server.domain.drawing.presentation.dto.request.DrawingSaveRequest;
import com.drawandyou.drawandyou_server.domain.drawing.presentation.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Drawing", description = "그림(드로잉) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drawings")
public class DrawingController {

    private final DrawingSaveService drawingSaveService;

    @Operation(summary = "그림 저장 및 AI 분석결과 조회", description = "사용자가 그린 그림을 데이터 베이스에 저장하고 AI 분석 결과를 반환하는 API 입니다.")
    @PostMapping("/analyze")
    public ApiResponse<DrawingAnalysisResponse> saveAndAnalyzeDrawing(@AuthenticationPrincipal Long userId,
                                                                      @RequestBody DrawingSaveRequest drawingSaveRequest){
        DrawingAnalysisResponse response = drawingSaveService.saveAndAnalyzeDrawing(userId, drawingSaveRequest);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.DRAWING_ANALYSIS_SUCCESS.getMessage(), response);
    }

}
