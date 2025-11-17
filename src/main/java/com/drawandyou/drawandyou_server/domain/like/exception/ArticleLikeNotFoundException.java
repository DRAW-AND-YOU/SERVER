package com.drawandyou.drawandyou_server.domain.like.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ArticleLikeNotFoundException extends CustomException {
    public ArticleLikeNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorMessage.ARTICLE_LIKE_NOT_FOUND.getMessage());
    }
}
