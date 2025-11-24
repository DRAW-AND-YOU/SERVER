package com.drawandyou.drawandyou_server.domain.diary.presentation.dto.request;

import com.drawandyou.drawandyou_server.domain.diary.domain.entity.enums.EmotionKeyword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record DiaryCreateRequest(
        // 제목 , 내용, 날짜, 감정 키워드
        @NotNull(message = "일기 작성일은 필수입니다.")
        LocalDateTime writtenAt,
        @NotNull(message = "키워드는 필수입니다.")
        EmotionKeyword keyword,
        @NotBlank(message = "제목은 비어있을 수 없습니다.")
        @Size(max = 50 , message = "제목은 최대 50자까지 입력 가능합니다.")
        String title,
        @NotBlank(message = "일기 내용을 입력해주세요.")
        @Size(max = 1000, message = "일기 내용은 최대 1000자까지 입력 가능합니다.")
        String content
) {
}
