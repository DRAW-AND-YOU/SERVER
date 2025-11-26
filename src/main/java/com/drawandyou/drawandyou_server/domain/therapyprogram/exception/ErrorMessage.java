package com.drawandyou.drawandyou_server.domain.therapyprogram.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    ALREADY_PARTICIPATED_IN_PROGRAM("이미 다른 프로그램에 참여중이므로 프로그램에 참여할 수 없습니다."),
    THERAPY_PROGRAM_NOT_FOUND("치유 프로그램을 찾을 수 없습니다."),
    NOT_ALL_COURSES_COMPLETED("모든 데일리 코스를 완료해야 프로그램을 완주할 수 있습니다."),
    THERAPY_PROGRAM_ACCESS_DENIED("치유 프로그램에 대한 접근권한이 존재하지 않습니다");
    private final String message;
}
