package com.drawandyou.drawandyou_server.domain.comment.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends CustomException {
    public CommentNotFoundException(){
        super(HttpStatus.NOT_FOUND, ErrorMessage.COMMENT_NOT_FOUND.getMessage());
    }
}
