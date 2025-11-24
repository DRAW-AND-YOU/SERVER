package com.drawandyou.drawandyou_server.global.client.fastapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    IMAGE_CREATE_FAIL("AI 이미지 생성에 실패하였습니다.");

    private final String message;
}
