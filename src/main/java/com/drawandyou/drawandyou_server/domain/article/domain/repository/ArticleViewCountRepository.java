package com.drawandyou.drawandyou_server.domain.article.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

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

    public Long increase(Long articleId){
        return redisTemplate.opsForValue().increment(generateKey(articleId));
    }
    private String generateKey(Long articleId){
        return String.format(KEY_FORMAT, articleId);
    }

}
