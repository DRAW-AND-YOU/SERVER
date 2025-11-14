package com.drawandyou.drawandyou_server.global.common.response;

import org.springframework.data.domain.Page;

public record PageInfo(
        long pageNumber,
        long pageSize,
        long totalElements,
        long totalPages,
        boolean first,
        boolean last,
        boolean empty
) {
    public static PageInfo from(Page<?> page) {
        return new PageInfo(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty()
        );
    }
}
