package com.drawandyou.drawandyou_server.domain.drawing.presentation.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {


    DRAWING_CREATE_SUCCESS("그림 저장이 완료되었습니다."),
    DRAWING_ANALYSIS_SUCCESS("AI 그림 분석에 성공하였습니다");

    private final String message;
}
