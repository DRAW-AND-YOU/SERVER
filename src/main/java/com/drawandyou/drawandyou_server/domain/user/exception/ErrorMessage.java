package com.drawandyou.drawandyou_server.domain.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    INVALID_PASSWORD("패스워드가 일치하지 않습니다."),
    USER_ALREADY_EXISTS("이미 존재하는 사용자입니다"),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다");

    private final String message;
}
