package com.drawandyou.drawandyou_server.domain.comment.presesntation.response;

import com.drawandyou.drawandyou_server.global.common.response.PageInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public record CommentPageResponse(
        List<CommentResponse> comments,
        PageInfo pageInfo

) {
    public static CommentPageResponse of(Page<CommentResponse> page){
        return new CommentPageResponse(
                page.getContent(),
                PageInfo.from(page)
        );
    }

    public static CommentPageResponse of(List<CommentResponse> comments, long pageNumber, int pageSize, long totalElements){
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        PageInfo pageInfo = new PageInfo(
                pageNumber,
                pageSize,
                totalElements,
                totalPages,
                pageNumber == 0,
                pageNumber >= totalPages - 1,
                comments.isEmpty()
        );

        return new CommentPageResponse(comments, pageInfo);
    }
}
