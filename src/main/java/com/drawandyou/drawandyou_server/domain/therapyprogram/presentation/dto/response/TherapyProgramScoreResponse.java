package com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.dto.response;

import com.drawandyou.drawandyou_server.domain.dailycourse.presentation.dto.DailyCourseScoreResponse;

import java.util.List;

public record TherapyProgramScoreResponse(
        List<DailyCourseScoreResponse> scores
) {
}
