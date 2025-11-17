package com.drawandyou.drawandyou_server.domain.like.domain.repository;

import com.drawandyou.drawandyou_server.domain.like.domain.entity.ArticleLikeCount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleLikeCountRepository extends JpaRepository<ArticleLikeCount, Long> {

    @Query(
            value = "update article_like_count set like_count = like_count + 1 " +
                    "where article_id = :articleId",
            nativeQuery = true
    )
    @Modifying
    int increase(@Param("articleId") Long articleId);

    @Query(
            value = "update article_like_count set like_count = like_count -1 " +
                    "where article_id = :articleId",
            nativeQuery = true
    )
    @Modifying
    int decrease(@Param("articleId") Long articleId);


}
