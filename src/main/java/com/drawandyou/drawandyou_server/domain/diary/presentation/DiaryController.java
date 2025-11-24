package com.drawandyou.drawandyou_server.domain.diary.presentation;

import com.drawandyou.drawandyou_server.domain.diary.application.service.DiaryService;
import com.drawandyou.drawandyou_server.domain.diary.presentation.dto.request.DiaryCreateRequest;
import com.drawandyou.drawandyou_server.domain.diary.presentation.dto.response.DiaryCalendarResponse;
import com.drawandyou.drawandyou_server.domain.diary.presentation.dto.response.DiaryResponse;
import com.drawandyou.drawandyou_server.domain.diary.presentation.message.ResponseMessage;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "DIARY", description = "일기 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(summary = "일기 작성완료 및 AI 이미지 결과 받기")
    @PostMapping
    public ApiResponse<DiaryResponse> createDiaryAndGetAiImage(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid DiaryCreateRequest request){

        DiaryResponse response = diaryService.writeDiary(userId, request);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.DIARY_CREATE_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "테스트용 API - 일기 작성완료(AI 결과 받지 않음)")
    @PostMapping("/test")
    public ApiResponse<DiaryResponse> createDiaryTest(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid DiaryCreateRequest request){

        DiaryResponse response = diaryService.writeDiaryTest(userId, request);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.DIARY_CREATE_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "일기 삭제")
    @DeleteMapping("/{diaryId}")
    public ApiResponse<Void> deleteDiary(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long diaryId) {

        diaryService.delete(userId, diaryId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.DIARY_DELETE_SUCCESS.getMessage());
    }

    @Operation(summary = "캘린더 날짜 범위별 일기 리스트 조회")
    @GetMapping("/calendars")
    public ApiResponse<DiaryCalendarResponse> getDiariesForCalendar(
            @AuthenticationPrincipal Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){

        DiaryCalendarResponse response = diaryService.getDiariesForCalendar(userId, startDate, endDate);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.DIARY_CALENDAR_GET_SUCCESS.getMessage(), response);
    }


    @Operation(summary = "일기 단건조회")
    @GetMapping("/{diaryId}")
    public ApiResponse<DiaryResponse> getDiary(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long diaryId) {

        DiaryResponse response = diaryService.getDiary(userId, diaryId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.DIARY_GET_SUCCESS.getMessage(), response);
    }
}
