package com.drawandyou.drawandyou_server.domain.diary.presentation.dto.response;

import com.drawandyou.drawandyou_server.domain.diary.domain.entity.Diary;

import java.time.LocalDateTime;

public record DiaryResponse(
        Long diaryId,
        LocalDateTime writtenAt,
        String keyword,
        String imageUrl,
        String title,
        String content
) {
    public static DiaryResponse from(Diary diary){
        return new DiaryResponse(
                diary.getId(),
                diary.getWrittenAt(),
                diary.getEmotionKeyword().getMessage(),
                diary.getImageUrl(),
                diary.getTitle(),
                diary.getContent()
        );
    }
}
