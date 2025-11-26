package com.drawandyou.drawandyou_server.domain.therapyprogram.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class TherapyProgramAccessDeniedException extends CustomException {
    public TherapyProgramAccessDeniedException() {
        super(HttpStatus.FORBIDDEN, ErrorMessage.THERAPY_PROGRAM_ACCESS_DENIED.getMessage());
    }
}
