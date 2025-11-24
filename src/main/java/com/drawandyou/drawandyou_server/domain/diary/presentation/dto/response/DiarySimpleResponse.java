package com.drawandyou.drawandyou_server.domain.diary.presentation.dto.response;

import java.time.LocalDateTime;

public record DiarySimpleResponse(
        Long diaryId,
        LocalDateTime writtenAt,
        String imageUrl
) {
}
