package com.drawandyou.drawandyou_server.global.client.fastapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * FastAPI 이미지 생성 응답 DTO
 * 생성된 이미지의 URL을 반환합니다.
 */
@Builder
public record DiaryImageResponse(
        @JsonProperty("image_url")
        String imageUrl
) {
}