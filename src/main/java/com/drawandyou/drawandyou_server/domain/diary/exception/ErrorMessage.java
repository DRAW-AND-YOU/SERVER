package com.drawandyou.drawandyou_server.domain.diary.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    DIARY_NOT_FOUND("일기를 찾을 수 없습니다."),
    NOT_DIARY_OWNER("일기에 대한 접근권한이 존재하지 않습니다.");
    private final String message;
}
