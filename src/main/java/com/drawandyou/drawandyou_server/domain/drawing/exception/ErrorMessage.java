package com.drawandyou.drawandyou_server.domain.drawing.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    DRAWING_NOT_FOUND("그림이 존재하지 않습니다.");

    private final String message;
}
