package com.drawandyou.drawandyou_server.domain.diary.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DiaryExistsException extends CustomException {
    public DiaryExistsException() {
        super(HttpStatus.CONFLICT, ErrorMessage.DIARY_ALREADY_EXISTS.getMessage());
    }
}
