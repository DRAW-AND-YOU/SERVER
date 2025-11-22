package com.drawandyou.drawandyou_server.domain.like.presentation.dto.response;

public record ArticleLikeResponse(
        Long userId,
        Boolean isLiked
) {

    public static ArticleLikeResponse of(Long userId, boolean isLiked){
        return new ArticleLikeResponse(
                userId,
                isLiked
        );
    }
}
