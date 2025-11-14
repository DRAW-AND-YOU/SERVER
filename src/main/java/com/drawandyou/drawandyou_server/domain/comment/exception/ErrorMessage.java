package com.drawandyou.drawandyou_server.domain.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    UNAUTHORIZED_COMMENT_DELETION("댓글을 삭제할 권한이 없습니다."),
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다.");
    private final String message;
}
