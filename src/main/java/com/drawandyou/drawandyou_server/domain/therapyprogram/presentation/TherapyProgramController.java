package com.drawandyou.drawandyou_server.domain.therapyprogram.presentation;

import com.drawandyou.drawandyou_server.domain.therapyprogram.application.service.TherapyProgramFindService;
import com.drawandyou.drawandyou_server.domain.therapyprogram.application.service.TherapyProgramService;
import com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.dto.response.OngoingProgramResponse;
import com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.dto.response.ParticipatedProgramIdResponse;
import com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.dto.response.TherapyProgramScoreResponse;
import com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.message.ResponseMessage;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "THERAPY PROGRAM", description = "치유 프로그램 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/programs")
public class TherapyProgramController {

    private final TherapyProgramService therapyProgramService;
    private final TherapyProgramFindService therapyProgramFindService;

    @Operation(summary = "치유 프로그램 등록하기")
    @PostMapping("/enroll")
    public ApiResponse<Void> enrollTherapyProgram(@AuthenticationPrincipal Long userId) {
        therapyProgramService.enrollTherapyProgram(userId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.THERAPY_PROGRAM_ENROLL_SUCCESS.getMessage());
    }

    @Operation(summary = "현재 프로그램의 진행 상태 조회", description = "현재 진행중인 프로그램의 완료 상태와, 앞으로 진행해야할 daily course 의 id 를 확인할 수 있습니다.")
    @GetMapping("/ongoing")
    public ApiResponse<OngoingProgramResponse> getOngoingProgramInfo(@AuthenticationPrincipal Long userId){
        OngoingProgramResponse response = therapyProgramFindService.getOngoingProgramInfo(userId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.ONGOING_PROGRAM_INFO_GET_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "치유 프로그램과 관련된 이미지 및 점수 변화 조회")
    @GetMapping("/{therapyProgramId}/scores")
    public ApiResponse<TherapyProgramScoreResponse> getTherapyProgramScores(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long therapyProgramId){
        TherapyProgramScoreResponse response = therapyProgramService.getTherapyProgramScores(userId, therapyProgramId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.THERAPY_PROGRAM_SCORES_GET_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "유저가 참가완료 한 치유프로그램 정보 조회")
    @GetMapping("/participated")
    public ApiResponse<ParticipatedProgramIdResponse> getTherapyProgramInfos(@AuthenticationPrincipal Long userId){
        ParticipatedProgramIdResponse response = therapyProgramFindService.getTherapyProgramInfos(userId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.THERAPY_PROGRAM_IDS_GET_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "치유 프로그램 완주하기")
    @PatchMapping("/{therapyProgramId}/complete")
    public ApiResponse<Void> completeTherapyProgram(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long therapyProgramId){
        therapyProgramService.completeTherapyProgram(userId, therapyProgramId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.THERAPY_PROGRAM_COMPLETE_SUCCESS.getMessage());
    }

}
