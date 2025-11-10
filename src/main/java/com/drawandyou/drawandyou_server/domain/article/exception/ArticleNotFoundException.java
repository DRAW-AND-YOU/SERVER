package com.drawandyou.drawandyou_server.domain.article.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ArticleNotFoundException extends CustomException {
    public ArticleNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorMessage.ARTICLE_NOT_FOUND.getMessage());
    }
}
