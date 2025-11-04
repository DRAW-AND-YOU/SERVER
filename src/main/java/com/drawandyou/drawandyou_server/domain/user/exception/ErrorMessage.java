package com.drawandyou.drawandyou_server.domain.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    INVALID_PASSWORD("패스워드가 일치하지 않습니다."),
    USER_ALREADY_EXISTS("이미 존재하는 사용자입니다"),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다"),
    NOT_SOCIAL_LOGIN_USER("소셜 로그인 사용자가 아닙니다."),
    PASSWORD_NOT_CORRECT("현재 비밀번호가 일치하지 않습니다.");

    private final String message;
}
