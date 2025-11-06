package com.drawandyou.drawandyou_server.domain.therapyprogram.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AlreadyParticipateInProgramException extends CustomException {
    public AlreadyParticipateInProgramException() {
        super(HttpStatus.BAD_REQUEST, ErrorMessage.ALREADY_PARTICIPATED_IN_PROGRAM.getMessage());
    }
}
