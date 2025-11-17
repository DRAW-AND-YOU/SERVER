package com.drawandyou.drawandyou_server.domain.like.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    ARTICLE_LIKE_NOT_FOUND("게시글 좋아요를 찾을 수 없습니다");
    private final String message;
}
