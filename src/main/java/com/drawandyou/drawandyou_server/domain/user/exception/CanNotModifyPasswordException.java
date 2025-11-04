package com.drawandyou.drawandyou_server.domain.user.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CanNotModifyPasswordException extends CustomException {
    public CanNotModifyPasswordException() {
        super(HttpStatus.BAD_REQUEST, ErrorMessage.PASSWORD_NOT_CORRECT.getMessage());
    }
}
