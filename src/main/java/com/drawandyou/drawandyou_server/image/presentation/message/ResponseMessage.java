package com.drawandyou.drawandyou_server.image.presentation.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {

    PRESIGNED_URL_CREATE_SUCCESS("Presigned URL 발급에 성공하였습니다.");

    private final String message;
}
