package com.drawandyou.drawandyou_server.domain.article.application.service;

import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleViewCountRepository;
import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleViewLockRepository;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.ArticleViewCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ArticleViewService {

    private final ArticleViewLockRepository articleViewLockRepository;
    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleViewCountBackUpProcessor articleViewCountBackUpProcessor;

    private static final int BACK_UP_BATCH_SIZE = 5;
    private static final Duration TTL = Duration.ofMinutes(10);

    public ArticleViewCountResponse increase(Long articleId, Long userId){

        // LOCK 획득 실패시
        if (!articleViewLockRepository.lock(articleId, userId, TTL)){
            Long count = articleViewCountRepository.read(articleId);
            return new ArticleViewCountResponse(count);

        }
        Long count = articleViewCountRepository.increase(articleId);
        if (count % BACK_UP_BATCH_SIZE == 0){
            articleViewCountBackUpProcessor.backUp(articleId, count);
        }
        return new ArticleViewCountResponse(count);
    }

    public ArticleViewCountResponse count(Long articleId){
        Long count = articleViewCountRepository.read(articleId);
        return new ArticleViewCountResponse(count);
    }
}
