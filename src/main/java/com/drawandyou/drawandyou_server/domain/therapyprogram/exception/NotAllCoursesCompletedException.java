package com.drawandyou.drawandyou_server.domain.therapyprogram.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NotAllCoursesCompletedException extends CustomException {
    public NotAllCoursesCompletedException() {
        super(HttpStatus.BAD_REQUEST, ErrorMessage.NOT_ALL_COURSES_COMPLETED.getMessage());
    }
}