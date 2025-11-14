package com.drawandyou.drawandyou_server.domain.comment.presesntation.response;

import java.util.List;

public record CommentPageResponse(
        List<CommentResponse> comments,
        Long commentCount

) {
    public static CommentPageResponse of(List<CommentResponse> comments, Long commentCount){
        return new CommentPageResponse(
                comments,
                commentCount
        );
    }
}
