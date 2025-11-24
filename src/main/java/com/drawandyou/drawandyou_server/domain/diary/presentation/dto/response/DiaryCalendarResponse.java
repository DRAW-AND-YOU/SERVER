package com.drawandyou.drawandyou_server.domain.diary.presentation.dto.response;

import java.util.List;

public record DiaryCalendarResponse(
        List<DiarySimpleResponse> diaries
) {
}
