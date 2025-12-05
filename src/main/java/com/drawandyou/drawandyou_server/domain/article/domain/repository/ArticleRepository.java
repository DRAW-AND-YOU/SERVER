package com.drawandyou.drawandyou_server.domain.article.domain.repository;

import com.drawandyou.drawandyou_server.domain.article.domain.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

    @Query("SELECT count(a.id) FROM Article a WHERE a.user.id = :userId")
    Long countByUserId(Long userId);
}
