package com.drawandyou.drawandyou_server.image.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class MimeTypeNotJPGException extends CustomException {
    public MimeTypeNotJPGException() {
        super(HttpStatus.BAD_REQUEST, ErrorMessage.MIM_TYPE_NOT_JPG_EXCEPTION.getMessage());
    }
}
