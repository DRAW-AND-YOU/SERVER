package com.drawandyou.drawandyou_server.domain.article.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class ArticleViewLockRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String KEY_FORMAT = "view::article::%s::user::%s::lock";

    // redis 에 , 특정 유저가 10분 이내에 게시글을 조회한 이력이 있는 경우 false 반환
    // 10분 내의 조회 이력이 없는 경우 값을 설정하고, true 반환
    public boolean lock(Long articleId, Long userId, Duration ttl){
        String key = generateKey(articleId, userId);
        return redisTemplate.opsForValue().setIfAbsent(key, "", ttl);
    }

    private String generateKey(Long articleId, Long userId){
        return KEY_FORMAT.formatted(articleId, userId);
    }




}
