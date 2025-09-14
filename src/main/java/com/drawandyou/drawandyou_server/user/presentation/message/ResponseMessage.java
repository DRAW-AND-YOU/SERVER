package com.drawandyou.drawandyou_server.user.presentation.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {

    TEST_SUCCESS("유저 테스트에 성공하였습니다");
    private final String message;
}
