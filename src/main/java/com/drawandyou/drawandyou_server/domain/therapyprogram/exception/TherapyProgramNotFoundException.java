package com.drawandyou.drawandyou_server.domain.therapyprogram.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class TherapyProgramNotFoundException extends CustomException {
    public TherapyProgramNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorMessage.THERAPY_PROGRAM_NOT_FOUND.getMessage());
    }
}
