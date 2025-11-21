package com.drawandyou.drawandyou_server.domain.article.presentation.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {

    ARTICLE_CREATE_SUCCESS("게시글 생성에 성공했습니다"),
    ARTICLE_DETAIL_SUCCESS("게시글 상세 조회에 성공했습니다"),
    ARTICLE_UPDATE_SUCCESS("게시글 수정에 성공했습니다"),
    ARTICLE_DELETE_SUCCESS("게시글 삭제에 성공했습니다"),
    ARTICLE_VIEW_COUNT_INCREASEMENT_SUCCESS("게시글 조회수 증가에 성공했습니다."),
    ARTICLE_VIEW_COUNT_GET_SUCCESS("게시글 조회수 조회에 성공했습니다."),
    ARTICLE_SCROLL_SUCCESS("게시글 목록 무한스크롤 조회에 성공했습니다"),
    MY_ARTICLE_SCROLL_SUCCESS("자신이 작성한 게시글 목록 무한스크롤 조회에 성공했습니다"),
    POPULAR_ARTICLE_GET_SUCCESS("인기 게시글 조회에 성공했습니다");

    private final String message;
}
