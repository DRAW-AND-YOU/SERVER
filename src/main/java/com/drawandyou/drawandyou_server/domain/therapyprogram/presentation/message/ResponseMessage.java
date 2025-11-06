package com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {

    THERAPY_PROGRAM_ENROLL_SUCCESS("치유 프로그램 등록에 성공하였습니다."),
    THERAPY_PROGRAM_COMPLETE_SUCCESS("치유 프로그램의 모든 코스 완주에 성공했습니다."),
    ONGOING_PROGRAM_INFO_GET_SUCCESS("진행중인 프로그램 정보 조회에 성공했습니다.");

    private final String message;
}
