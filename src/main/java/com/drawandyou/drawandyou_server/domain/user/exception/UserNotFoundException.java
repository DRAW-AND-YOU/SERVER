package com.drawandyou.drawandyou_server.domain.user.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND.getMessage());
    }
}
