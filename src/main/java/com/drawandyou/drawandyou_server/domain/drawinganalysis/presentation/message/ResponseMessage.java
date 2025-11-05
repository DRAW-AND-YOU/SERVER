package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {

    DRAWING_ANALYSIS_SUCCESS("AI 그림 분석에 성공했습니다."),
    DRAWING_ANALYSIS_DETAIL_GET_SUCCESS("그림 분석 상세조회에 성공했습니다.");

    private final String message;
}
