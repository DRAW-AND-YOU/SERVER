package com.drawandyou.drawandyou_server.global.common.exception.response;

public record ParseJsonErrorResponse(
        Integer line,
        Integer column,
        String message
) implements JsonErrorResponseDetail {
}
