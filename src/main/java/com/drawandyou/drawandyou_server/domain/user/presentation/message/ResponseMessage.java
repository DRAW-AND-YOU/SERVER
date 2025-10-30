package com.drawandyou.drawandyou_server.domain.user.presentation.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {

    TEST_SUCCESS("유저 테스트에 성공하였습니다"),
    USER_SIGNUP_SUCCESS("회원가입에 성공했습니다"),
    USER_SIGNIN_SUCCESS("로그인에 성공했습니다"),
    USER_INFO_SUCCESS("사용자 정보 조회에 성공했습니다"),
    TOKEN_ISSUE_SUCCESS("JWT 토큰 발급에 성공했습니다");

    private final String message;
}
