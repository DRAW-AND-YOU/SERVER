package com.drawandyou.drawandyou_server.domain.diary.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DiaryNotFoundException extends CustomException {
    public DiaryNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorMessage.DIARY_NOT_FOUND.getMessage());
    }
}
