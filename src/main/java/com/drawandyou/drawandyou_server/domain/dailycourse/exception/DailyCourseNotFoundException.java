package com.drawandyou.drawandyou_server.domain.dailycourse.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DailyCourseNotFoundException extends CustomException {
    public DailyCourseNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorMessage.DAILY_COURSE_NOT_FOUND.getMessage());
    }
}
