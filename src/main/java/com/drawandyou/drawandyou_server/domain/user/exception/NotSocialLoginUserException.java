package com.drawandyou.drawandyou_server.domain.user.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NotSocialLoginUserException extends CustomException {
    public NotSocialLoginUserException() {
        super(HttpStatus.BAD_REQUEST, ErrorMessage.NOT_SOCIAL_LOGIN_USER.getMessage());
    }
}
