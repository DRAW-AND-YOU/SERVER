package com.drawandyou.drawandyou_server.domain.article.domain.repository;

import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.ArticleResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepositoryCustom {

    List<ArticleResponse> findAllInfiniteScroll(Long limit);

    List<ArticleResponse> findAllInfiniteScroll(Long limit, LocalDateTime lastCreatedAt, Long lastArticleId);
}