package com.drawandyou.drawandyou_server.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    INVALID_PASSWORD("패스워드가 일치하지 않습니다."),
    USER_NAME_ALREADY_EXISTS("같은 사용자 명이 존재합니다."),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다");

    private final String message;
}
