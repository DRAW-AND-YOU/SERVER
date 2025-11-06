package com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.dto.response;

public record OngoingProgramResponse(
        Integer completedCourseCount,
        Integer totalDays,
        Long toParticipateDailyCourseId
) {
}
