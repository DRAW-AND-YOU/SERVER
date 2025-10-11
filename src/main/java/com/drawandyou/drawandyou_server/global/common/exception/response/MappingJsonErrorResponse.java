package com.drawandyou.drawandyou_server.global.common.exception.response;

public record MappingJsonErrorResponse(
        String field,
        String message
) implements JsonErrorResponseDetail {
}
