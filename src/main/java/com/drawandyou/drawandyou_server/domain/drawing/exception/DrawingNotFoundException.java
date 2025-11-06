package com.drawandyou.drawandyou_server.domain.drawing.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DrawingNotFoundException extends CustomException {
    public DrawingNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorMessage.DRAWING_NOT_FOUND.getMessage());
    }
}
