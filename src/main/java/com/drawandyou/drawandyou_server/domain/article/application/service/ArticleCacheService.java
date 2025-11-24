package com.drawandyou.drawandyou_server.domain.article.application.service;

import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleViewCountRepository;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.PopularArticleListResponse;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.PopularArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArticleCacheService {

    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticlePopularityService articlePopularityService;

    /**
     * Redis Sorted Set 기반 주간 인기 게시글 조회
     */
    @Transactional(readOnly = true)
    public PopularArticleListResponse getWeeklyPopularPosts() {

        List<PopularArticleResponse> popularArticles = articlePopularityService.getWeeklyPopularArticles();

        // 조회수 정보 추가
        List<Long> articleIds = popularArticles.stream()
                .map(PopularArticleResponse::articleId)
                .toList();

        Map<Long, Long> viewCountMap = articleViewCountRepository.readMultiple(articleIds);

        // 조회수 정보를 포함한 응답 생성
        List<PopularArticleResponse> list = popularArticles.stream()
                .map(article -> new PopularArticleResponse(
                        article.articleId(),
                        article.articleImageUrl(),
                        article.title(),
                        article.authorName(),
                        article.authorImageUrl(),
                        viewCountMap.getOrDefault(article.articleId(), 0L),
                        article.likeCount(),
                        article.commentCount(),
                        article.createdAt()
                ))
                .toList();

        return new PopularArticleListResponse(list);
    }
}
