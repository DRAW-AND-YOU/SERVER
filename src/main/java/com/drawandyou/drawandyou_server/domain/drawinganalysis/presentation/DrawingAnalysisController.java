package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation;

import com.drawandyou.drawandyou_server.domain.drawinganalysis.application.service.DrawingAnalysisFindService;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.request.DrawingAnalysisRequest;

import com.drawandyou.drawandyou_server.domain.drawinganalysis.application.service.DrawingAnalyzeService;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.request.enums.AnalysisSortType;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.DrawingAnalysisAndRecommendationResponse;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.DrawingAnalysisDetailResponse;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.DrawingAnalysisListResponse;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.message.ResponseMessage;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "DRAWING ANALYSIS", description = "그림 분석 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drawing-analysis")
public class DrawingAnalysisController {

    private final DrawingAnalyzeService drawingAnalyzeService;
    private final DrawingAnalysisFindService drawingAnalysisFindService;

    @Operation(summary = "그림 분석결과 및 컨텐츠 추천결과 반환", description = "사용자가 그린 그림을 데이터 베이스에 저장하고 Fast API로부터 AI 분석 결과를 반환하는 API 입니다.")
    @PostMapping
    public ApiResponse<DrawingAnalysisAndRecommendationResponse> analyzeDrawingAndGetContentRecommendation(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid DrawingAnalysisRequest drawingAnalysisRequest){
        DrawingAnalysisAndRecommendationResponse response = drawingAnalyzeService.analyzeDrawingAndGetContentRecommendation(userId, drawingAnalysisRequest);
        return ApiResponse.success(HttpStatus.OK,  ResponseMessage.DRAWING_ANALYSIS_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "그림 분석 결과 상세조회", description = "사용자가 그린 그림 분석결과에 대해 상세조회를 할 수 있는 API입니다.")
    @GetMapping("/{drawingAnalysisId}")
    public ApiResponse<DrawingAnalysisDetailResponse> getDrawingAnalysisDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long drawingAnalysisId){

        DrawingAnalysisDetailResponse response = drawingAnalysisFindService.getDrawingAnalysisDetail(userId, drawingAnalysisId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.DRAWING_ANALYSIS_DETAIL_GET_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "그림 분석 결과 리스트조회(페이지네이션)", description = "사용자가 그린 그림 분석결과를 리스트로 조회할 수 있는 API 입니다.")
    @GetMapping
    public ApiResponse<DrawingAnalysisListResponse> getDrawingAnalysisList(
            @AuthenticationPrincipal Long userId,
            @RequestParam AnalysisSortType sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size){

        DrawingAnalysisListResponse response = drawingAnalysisFindService.getDrawingAnalysisList(sortBy, userId, page, size);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.DRAWING_ANALYSIS_LIST_GET_SUCCESS.getMessage(), response);
    }
}
