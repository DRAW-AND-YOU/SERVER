package com.drawandyou.drawandyou_server.domain.therapyprogram.presentation;

import com.drawandyou.drawandyou_server.domain.therapyprogram.application.service.TherapyProgramFindService;
import com.drawandyou.drawandyou_server.domain.therapyprogram.application.service.TherapyProgramService;
import com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.dto.response.OngoingProgramResponse;
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
}
