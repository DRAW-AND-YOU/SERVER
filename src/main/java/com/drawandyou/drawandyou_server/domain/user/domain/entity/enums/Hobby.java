package com.drawandyou.drawandyou_server.domain.user.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Hobby {
    MUSIC("음악"),
    MOVIE_DRAMA("영화 / 드라마"),
    BOOK_WRITING("책 / 글쓰기"),
    TRAVEL("여"),
    FITNESS("운동 / 피트니스"),
    ART_DESIGN("미술 / 디자인"),
    GAME("게"),
    FOOD_COOKING("음식 / 요리"),
    NATURE_ANIMAL("자연 / 동"),
    FASHION_STYLE("패션 / 스타일");

    private final String displayName;
}
