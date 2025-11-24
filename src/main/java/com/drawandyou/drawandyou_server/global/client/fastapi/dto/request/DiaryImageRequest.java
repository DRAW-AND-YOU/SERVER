package com.drawandyou.drawandyou_server.global.client.fastapi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * FastAPI 이미지 생성 요청 DTO
 * 일기 키워드, 제목, 내용을 기반으로 이미지를 생성합니다.
 */
public record DiaryImageRequest(
        @JsonProperty("keyword")
        String keyword,

        @JsonProperty("title")
        String title,

        @JsonProperty("content")
        String content
) {
}