package com.drawandyou.drawandyou_server.domain.article.application.service;

import com.drawandyou.drawandyou_server.domain.article.application.service.dto.ViewResult;
import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleViewCountRepository;
import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleViewLockRepository;
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

    public ViewResult increase(Long articleId, Long userId){

        // LOCK 획득 실패시(즉, 조회수 증가시킬 수 없는 경우)
        if (!articleViewLockRepository.lock(articleId, userId, TTL)){
            return new ViewResult(articleViewCountRepository.read(articleId), false);
        }

        Long count = articleViewCountRepository.increase(articleId);
        if (count % BACK_UP_BATCH_SIZE == 0){
            articleViewCountBackUpProcessor.backUp(articleId, count);
        }
        return new ViewResult(count, true);
    }

    public Long count(Long articleId){
        return articleViewCountRepository.read(articleId);

    }
}
