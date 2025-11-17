package com.drawandyou.drawandyou_server.domain.comment.presesntation.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {

    COMMENT_READ_SUCCESS("댓글 조회에 성공했습니다."),
    COMMENT_CREATE_SUCCESS("댓글 작성에 성공했습니다."),
    COMMENT_DELETE_SUCCESS("댓글 삭제에 성공했습니다."),
    COMMENT_PAGINATION_GET_SUCCESS("댓글 목록 페이지네이션 조회에 성공했습니다."),
    COMMENT_COUNT_GET_SUCCESS("게시글의 댓글 수 조회에 성공했습니다.");

    private final String message;
}
