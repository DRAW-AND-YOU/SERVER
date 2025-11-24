package com.drawandyou.drawandyou_server.domain.diary.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmotionKeyword {

    HAPPINESS("행복"),
    EXPECTATION("기대"),
    LOVE("사랑"),
    PEACE("평온"),
    CONCERN("걱정"),
    PRIDE("뿌듯"),
    FATIGUE("피곤"),
    SADNESS("슬픔"),
    LONELINESS("외로움"),
    ANGER("화남"),
    CONFUSION("혼란"),
    DEPRESSION("우울");

    private final String message;

}
