package com.drawandyou.drawandyou_server.domain.user.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends CustomException {
    public UserAlreadyExistsException() {
        super(HttpStatus.CONFLICT, ErrorMessage.USER_NAME_ALREADY_EXISTS.getMessage());
    }
}
