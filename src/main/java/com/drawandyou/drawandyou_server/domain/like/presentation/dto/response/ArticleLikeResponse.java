package com.drawandyou.drawandyou_server.domain.like.presentation.dto.response;

public record ArticleLikeResponse(
        Boolean isLiked
) {

    public static ArticleLikeResponse from(boolean isLiked){
        return new ArticleLikeResponse(isLiked);
    }
}
