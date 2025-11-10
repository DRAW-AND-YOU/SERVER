package com.drawandyou.drawandyou_server.domain.article.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ArticleNotModifiableException extends CustomException {
    public ArticleNotModifiableException() {
        super(HttpStatus.FORBIDDEN, ErrorMessage.ARTICLE_NOT_MODIFIABLE.getMessage());
    }
}
