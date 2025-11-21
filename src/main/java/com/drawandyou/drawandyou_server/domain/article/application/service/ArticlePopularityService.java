package com.drawandyou.drawandyou_server.domain.article.application.service;

import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleRepository;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.PopularArticleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticlePopularityService {

    private final StringRedisTemplate redisTemplate;
    private final ArticleRepository articleRepository;

    private static final String WEEKLY_POPULAR_KEY_PREFIX = "article:popular:weekly:";
    private static final String ARTICLE_CREATED_TIME_KEY = "article:created:";
    private static final int TOP_N = 10;
    private static final int WEEKLY_RETENTION_DAYS = 7;

    /**
     * 게시글 조회 시 점수 증가
     */
    public void incrementViewScore(Long articleId) {
        if (!isArticleWithinWeek(articleId)) {
            return; // 일주일이 지난 게시글은 점수 증가 X
        }

        String key = getCurrentWeeklyKey();
        double scoreIncrement = 1.0; // 조회 시 1점

        redisTemplate.opsForZSet().incrementScore(key, articleId.toString(), scoreIncrement);

        // 키 만료 시간 설정 (7일 + 1일 여유)
        redisTemplate.expire(key, Duration.ofDays(8));

        log.debug("조회 점수 증가 - articleId: {}, score: +{}", articleId, scoreIncrement);
    }

    /**
     * 게시글 좋아요 시 점수 증가
     */
    public void incrementLikeScore(Long articleId) {
        if (!isArticleWithinWeek(articleId)) {
            return;
        }

        String key = getCurrentWeeklyKey();
        double scoreIncrement = 3.0; // 좋아요 시 3점

        redisTemplate.opsForZSet().incrementScore(key, articleId.toString(), scoreIncrement);
        redisTemplate.expire(key, Duration.ofDays(8));

        log.debug("좋아요 점수 증가 - articleId: {}, score: +{}", articleId, scoreIncrement);
    }
    
    /**
     * 주간 인기 게시글 TOP 10 조회
     */
    @Transactional(readOnly = true)
    public List<PopularArticleResponse> getWeeklyPopularArticles() {
        String key = getCurrentWeeklyKey();

        // Redis에서 상위 10개 게시글 ID와 점수 조회
        Set<ZSetOperations.TypedTuple<String>> topArticlesWithScores =
            redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, TOP_N - 1);

        if (topArticlesWithScores == null || topArticlesWithScores.isEmpty()) {
            log.info("인기 게시글이 없습니다.");
            return Collections.emptyList();
        }

        // Article ID 리스트 추출 (순서 유지)
        List<Long> articleIds = topArticlesWithScores.stream()
            .map(tuple -> Long.parseLong(Objects.requireNonNull(tuple.getValue())))
            .toList();

        List<PopularArticleResponse> articles = articleRepository
            .findTop10ByIdIn(articleIds);

        // Redis 순서대로 정렬 및 점수 정보 포함
        return articleIds.stream()
            .map(id -> articles.stream()
                .filter(article -> article.articleId().equals(id))
                .findFirst()
                .orElse(null))
            .filter(Objects::nonNull)
            .toList();
    }
    
    /**
     * 게시글이 생성된 지 일주일 이내인지 확인
     */
    private boolean isArticleWithinWeek(Long articleId) {
        // 게시글 생성 시간을 Redis 캐시나 DB에서 확인
        LocalDateTime createdAt = getArticleCreatedTime(articleId);
        if (createdAt == null) {
            return false;
        }
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(WEEKLY_RETENTION_DAYS);
        return createdAt.isAfter(oneWeekAgo);
    }

    /**
     * 게시글 생성 시간 조회 (캐시 활용)
     */
    private LocalDateTime getArticleCreatedTime(Long articleId) {
        String cacheKey = ARTICLE_CREATED_TIME_KEY + articleId;
        String cached = redisTemplate.opsForValue().get(cacheKey);

        if (cached != null) {
            return LocalDateTime.parse(cached);
        }

        // 캐시 히트 실패시 db 조회후 캐시 저장
        return articleRepository.findById(articleId)
            .map(article -> {
                LocalDateTime createdAt = article.getCreatedAt();
                redisTemplate.opsForValue().set(
                    cacheKey,
                    createdAt.toString(),
                    Duration.ofDays(8)
                );
                return createdAt;
            })
            .orElse(null);
    }
    
    /**
     * 현재 주간 키 생성
     */
    private String getCurrentWeeklyKey() {
        // 주 단위로 키를 구분하여 관리
        LocalDate now = LocalDate.now();
        LocalDate monday = now.minusDays(now.getDayOfWeek().getValue() - 1);
        return WEEKLY_POPULAR_KEY_PREFIX + monday;
    }
    
}