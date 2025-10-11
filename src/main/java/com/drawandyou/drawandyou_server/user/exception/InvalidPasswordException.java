package com.drawandyou.drawandyou_server.user.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends CustomException {
    public InvalidPasswordException() {
        super(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_PASSWORD.getMessage());
    }
}
