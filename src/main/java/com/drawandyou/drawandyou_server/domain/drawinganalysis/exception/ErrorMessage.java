package com.drawandyou.drawandyou_server.domain.drawinganalysis.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    CAN_NOT_VIEW_DRAWING_ANALYSIS("그림 분석 결과를 볼 수 있는 권한이 없습니다"),
    DRAWING_ANALYSIS_NOT_FOUND("그림 분석 결과가 존재하지 않습니다.");

    private final String message;


}
