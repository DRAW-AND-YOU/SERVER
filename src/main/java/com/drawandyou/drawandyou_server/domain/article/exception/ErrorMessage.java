package com.drawandyou.drawandyou_server.domain.article.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    ARTICLE_NOT_FOUND("게시글을 찾을 수 없습니다."),
    ARTICLE_NOT_MODIFIABLE("게시글을 수정할 권한이 존재하지 않습니다."),
    ARTICLE_NOT_DELETABLE("게시글을 삭제할 권한이 존재하지 않습니다");

    private final String message;
}
