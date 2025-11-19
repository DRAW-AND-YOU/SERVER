package com.drawandyou.drawandyou_server.domain.article.application.service;

import com.drawandyou.drawandyou_server.domain.article.domain.entity.Article;
import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleRepository;
import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleViewCountRepository;
import com.drawandyou.drawandyou_server.domain.article.exception.ArticleNotFoundException;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.ArticleDetailResponse;
import com.drawandyou.drawandyou_server.domain.articleimage.domain.entity.ArticleImage;
import com.drawandyou.drawandyou_server.domain.articleimage.domain.repository.ArticleImageRepository;
import com.drawandyou.drawandyou_server.domain.like.domain.entity.ArticleLikeCount;
import com.drawandyou.drawandyou_server.domain.like.domain.repository.ArticleLikeCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleReadService {

    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;
    private final ArticleViewCountRepository articleViewCountRepository;



    public ArticleDetailResponse getArticle(Long articleId) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(ArticleNotFoundException::new);

        ArticleImage articleImage = articleImageRepository.findByArticle(article);

        Long likeCount = articleLikeCountRepository.findByArticleId(articleId)
                .map(ArticleLikeCount::getLikeCount)
                .orElse(0L);

        Long viewCount = articleViewCountRepository.read(articleId);

        return ArticleDetailResponse.toResponse(article, articleImage,viewCount, likeCount);
    }
}
