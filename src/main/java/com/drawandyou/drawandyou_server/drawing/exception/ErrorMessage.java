package com.drawandyou.drawandyou_server.drawing.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    MIM_TYPE_NOT_JPG_EXCEPTION("mime type 이 image/jpg 가 아닙니다.");

    private final String message;
}
