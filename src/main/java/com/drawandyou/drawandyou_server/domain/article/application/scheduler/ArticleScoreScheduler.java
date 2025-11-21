package com.drawandyou.drawandyou_server.domain.article.application.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArticleScoreScheduler {

    private final StringRedisTemplate redisTemplate;
    private static final String WEEKLY_POPULAR_KEY_PREFIX = "article:popular:weekly:";

    /**
     * 매일 새벽 3시에 오래된 주간 인기글 데이터 정리
     * 7일 이상 된 키를 삭제하여 Redis 메모리 관리
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupOldPopularityData() {
        log.info("오래된 인기글 데이터 정리 시작");

        try {
            // 현재 날짜 기준으로 7일 이전 키 패턴 생성
            LocalDate cutoffDate = LocalDate.now().minusDays(7);

            // Redis에서 모든 주간 인기글 키 조회
            Set<String> keys = redisTemplate.keys(WEEKLY_POPULAR_KEY_PREFIX + "*");

            if (keys != null && !keys.isEmpty()) {
                // 7일 이상 된 키 필터링
                Set<String> keysToDelete = keys.stream()
                    .filter(key -> {
                        String dateStr = key.replace(WEEKLY_POPULAR_KEY_PREFIX, "");
                        try {
                            LocalDate keyDate = LocalDate.parse(dateStr);
                            return keyDate.isBefore(cutoffDate);
                        } catch (Exception e) {
                            log.warn("날짜 파싱 실패: {}", key);
                            return true; // 파싱 실패한 키는 삭제
                        }
                    })
                    .collect(Collectors.toSet());

                // 오래된 키 삭제
                if (!keysToDelete.isEmpty()) {
                    Long deletedCount = redisTemplate.delete(keysToDelete);
                    log.info("오래된 인기글 키 {} 개 삭제 완료", deletedCount);
                }
            }

            // 게시글 생성 시간 캐시도 정리
            cleanupArticleCreatedTimeCache();

        } catch (Exception e) {
            log.error("오래된 인기글 데이터 정리 실패", e);
        }
    }

    /**
     * 게시글 생성 시간 캐시 정리
     * 7일 이상 된 캐시 삭제
     */
    private void cleanupArticleCreatedTimeCache() {
        try {
            Set<String> createdTimeKeys = redisTemplate.keys("article:created:*");

            if (createdTimeKeys != null && !createdTimeKeys.isEmpty()) {
                // TTL이 설정되지 않은 키들에 대해 TTL 설정
                for (String key : createdTimeKeys) {
                    Long ttl = redisTemplate.getExpire(key);
                    if (ttl != null && ttl == -1) { // TTL이 설정되지 않은 경우
                        redisTemplate.expire(key, java.time.Duration.ofDays(8));
                    }
                }
                log.debug("게시글 생성 시간 캐시 TTL 설정 완료");
            }
        } catch (Exception e) {
            log.warn("게시글 생성 시간 캐시 정리 중 오류", e);
        }
    }
}
