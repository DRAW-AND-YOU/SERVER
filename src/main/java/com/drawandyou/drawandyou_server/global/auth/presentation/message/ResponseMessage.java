package com.drawandyou.drawandyou_server.global.auth.presentation.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {

    USER_SIGNUP_SUCCESS("회원가입에 성공했습니다"),
    USER_SIGNIN_SUCCESS("로그인에 성공했습니다");
    private final String message;
}
