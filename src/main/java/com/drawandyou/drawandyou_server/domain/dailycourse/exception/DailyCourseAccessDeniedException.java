package com.drawandyou.drawandyou_server.domain.dailycourse.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DailyCourseAccessDeniedException extends CustomException {
    public DailyCourseAccessDeniedException() {
        super(HttpStatus.FORBIDDEN, ErrorMessage.DAILY_COURSE_ACCESS_DENIED.getMessage());
    }
}
