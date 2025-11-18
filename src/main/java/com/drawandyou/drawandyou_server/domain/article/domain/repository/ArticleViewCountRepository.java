package com.drawandyou.drawandyou_server.domain.article.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ArticleViewCountRepository {

    private final StringRedisTemplate redisTemplate;

    // view:article::{article_id}::view_count
    private static final String KEY_FORMAT = "view:article:%d:view_count";

    public Long read(Long articleId){
        String result = redisTemplate.opsForValue().get(generateKey(articleId));
        return result == null ? 0L : Long.parseLong(result);
    }

    /**
     * 여러 게시글의 조회수를 한 번에 조회 (MGET 사용)
     * @param articleIds 조회할 게시글 ID 리스트
     * @return Map<ArticleId, ViewCount>
     */
    public Map<Long, Long> readMultiple(List<Long> articleIds) {
        if (articleIds == null || articleIds.isEmpty()) {
            return new HashMap<>();
        }

        List<String> keys = articleIds.stream()
                .map(this::generateKey)
                .toList();

        List<String> values = redisTemplate.opsForValue().multiGet(keys);

        Map<Long, Long> viewCountMap = new HashMap<>();
        for (int i = 0; i < articleIds.size(); i++) {
            Long articleId = articleIds.get(i);
            String value = values != null ? values.get(i) : null;
            Long viewCount = value == null ? 0L : Long.parseLong(value);
            viewCountMap.put(articleId, viewCount);
        }

        return viewCountMap;
    }

    public Long increase(Long articleId){
        return redisTemplate.opsForValue().increment(generateKey(articleId));
    }

    private String generateKey(Long articleId){
        return String.format(KEY_FORMAT, articleId);
    }

}
