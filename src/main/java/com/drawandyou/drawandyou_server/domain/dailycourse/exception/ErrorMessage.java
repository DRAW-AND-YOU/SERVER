package com.drawandyou.drawandyou_server.domain.dailycourse.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    DAILY_COURSE_NOT_FOUND("데일리 코스를 찾을 수 없습니다"),
    DAILY_COURSE_ACCESS_DENIED("데일리 코스에 대한 권한이 존재하지 않습니다.");
    private final String message;
}
