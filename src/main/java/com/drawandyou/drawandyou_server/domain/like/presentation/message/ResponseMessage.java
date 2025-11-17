package com.drawandyou.drawandyou_server.domain.like.presentation.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {

    ARTICLE_LIKE_GET_SUCCESS("게시글 좋아요 조회에 성공했습니다."),
    ARTICLE_LIKE_SUCCESS("게시글 좋아요에 성공했습니다."),
    ARTICLE_UNLIKE_SUCCESS("게시글 좋아요 취소에 성공했습니다");
    private final String message;
}
