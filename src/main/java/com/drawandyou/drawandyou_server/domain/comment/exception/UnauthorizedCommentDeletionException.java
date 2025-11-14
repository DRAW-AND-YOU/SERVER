package com.drawandyou.drawandyou_server.domain.comment.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UnauthorizedCommentDeletionException extends CustomException {
    public UnauthorizedCommentDeletionException(){
        super(HttpStatus.FORBIDDEN, ErrorMessage.UNAUTHORIZED_COMMENT_DELETION.getMessage());
    }
}
