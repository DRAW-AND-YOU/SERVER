package com.drawandyou.drawandyou_server.domain.article.domain.repository;

import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.ArticleResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepositoryCustom {

    List<ArticleResponse> findAllInfiniteScroll(Long userId, Long limit, LocalDateTime lastCreatedAt, Long lastArticleId);
}