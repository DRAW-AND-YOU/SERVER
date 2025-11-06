package com.drawandyou.drawandyou_server.domain.dailycourse.presentation;

import com.drawandyou.drawandyou_server.domain.dailycourse.application.service.DailyCourseService;
import com.drawandyou.drawandyou_server.domain.dailycourse.presentation.message.ResponseMessage;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "DAILY COURSE", description = "치유 프로그램 코스(1일차~5일차) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class DailyCourseController {

    private final DailyCourseService dailyCourseService;

    @Operation(summary = "데일리 코스 완료하기", description = "각 일자별 데일리코스를 완료한 후 호출하는 API 입니다. 그림 분석 결과 조회후 호출하시면 됩니다.")
    @PatchMapping("/{courseId}/complete")
    public ApiResponse<Void> completeDailyCourse(@AuthenticationPrincipal Long userId,
                                                 @PathVariable(name = "courseId") Long dailyCourseId,
                                                 @RequestParam Long drawingId){
        dailyCourseService.completeDailyCourse(userId, dailyCourseId, drawingId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.DAILY_COURSE_COMPLETE_SUCCESS.getMessage());
    }
}
