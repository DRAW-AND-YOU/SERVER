package com.drawandyou.drawandyou_server.domain.dailycourse.presentation.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {

    DAILY_COURSE_COMPLETE_SUCCESS("데일리 코스 참여에 성공하였습니다.");
    private final String message;
}
