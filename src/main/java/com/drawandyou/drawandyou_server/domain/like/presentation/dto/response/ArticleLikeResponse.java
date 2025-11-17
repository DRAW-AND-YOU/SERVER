package com.drawandyou.drawandyou_server.domain.like.presentation.dto.response;


import com.drawandyou.drawandyou_server.domain.like.domain.entity.ArticleLike;

import java.time.LocalDateTime;

public record ArticleLikeResponse(
        Long articleLikeId,
        Long articleId,
        Long userId,
        LocalDateTime createdAt
) {

    public static ArticleLikeResponse from(ArticleLike articleLike){
        return new ArticleLikeResponse(
                articleLike.getId(),
                articleLike.getArticleId(),
                articleLike.getUserId(),
                articleLike.getCreatedAt()
        );
    }
}
