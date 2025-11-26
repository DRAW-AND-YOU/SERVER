package com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.dto.response;

public record OngoingProgramResponse(
        Long therapyProgramId,
        Integer completedCourseCount,
        Integer totalDays,
        Long toParticipateDailyCourseId,
        Boolean isParticipated
) {
}
