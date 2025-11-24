package com.drawandyou.drawandyou_server.domain.diary.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NotDiaryOwnerException extends CustomException {
    public NotDiaryOwnerException() {
        super(HttpStatus.FORBIDDEN, ErrorMessage.NOT_DIARY_OWNER.getMessage());
    }
}
