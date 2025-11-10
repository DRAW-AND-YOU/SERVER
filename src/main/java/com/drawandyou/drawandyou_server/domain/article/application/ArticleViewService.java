package com.drawandyou.drawandyou_server.domain.article.application;

import com.drawandyou.drawandyou_server.domain.article.application.service.ArticleViewCountBackUpProcessor;
import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleViewCountRepository;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.ArticleViewCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleViewService {

    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleViewCountBackUpProcessor articleViewCountBackUpProcessor;
    private static final int BACK_UP_BATCH_SIZE = 100;

    public ArticleViewCountResponse increase(Long articleId){
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
