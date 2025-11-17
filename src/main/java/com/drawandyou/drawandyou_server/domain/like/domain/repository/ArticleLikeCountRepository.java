package com.drawandyou.drawandyou_server.domain.like.domain.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.drawandyou.drawandyou_server.domain.like.domain.entity.ArticleLikeCount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

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


    Optional<ArticleLikeCount> findByArticleId(Long articleId);
}
