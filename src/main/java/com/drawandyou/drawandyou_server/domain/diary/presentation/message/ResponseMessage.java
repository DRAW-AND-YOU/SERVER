package com.drawandyou.drawandyou_server.domain.diary.presentation.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {

    DIARY_CREATE_SUCCESS("일기 작성에 성공했습니다."),
    DIARY_DELETE_SUCCESS("일기 삭제에 성공했습니다."),
    DIARY_GET_SUCCESS("일기 단건 조회에 성공했습니다."),
    DIARY_CALENDAR_GET_SUCCESS("날짜 범위 별 일기 리스트 조회에 성공했습니다."),
    DIARY_UPDATE_SUCCESS("일기 수정에 성공했습니다.");
    private final String message;
}
