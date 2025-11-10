package com.drawandyou.drawandyou_server.domain.articleimage.domain.repository;

import com.drawandyou.drawandyou_server.domain.article.domain.entity.Article;
import com.drawandyou.drawandyou_server.domain.articleimage.domain.entity.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {
    ArticleImage findByArticle(Article article);

    void deleteAllByArticle(Article article);
}
