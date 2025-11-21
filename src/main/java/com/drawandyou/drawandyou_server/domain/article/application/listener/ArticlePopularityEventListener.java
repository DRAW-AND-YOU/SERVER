package com.drawandyou.drawandyou_server.domain.article.application.listener;

import com.drawandyou.drawandyou_server.domain.article.application.service.ArticlePopularityService;
import com.drawandyou.drawandyou_server.domain.article.domain.event.ArticleViewEvent;
import com.drawandyou.drawandyou_server.domain.like.domain.event.ArticleLikeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticlePopularityEventListener {

    private final ArticlePopularityService popularityService;

    /**
     * 게시글 조회 이벤트 처리
     */
    @Async
    @EventListener
    public void handleArticleView(ArticleViewEvent event) {
        try {
            popularityService.incrementViewScore(event.getArticleId());
            log.debug("게시글 조회 점수 업데이트 완료 - articleId: {}", event.getArticleId());
        } catch (Exception e) {
            log.error("게시글 조회 점수 업데이트 실패 - articleId: {}", event.getArticleId(), e);
        }
    }

    /**
     * 게시글 좋아요 이벤트 처리
     */
    @Async
    @EventListener
    public void handleArticleLike(ArticleLikeEvent event) {
        try {
            if (event.isLiked()) {
                popularityService.incrementLikeScore(event.getArticleId());
                log.debug("게시글 좋아요 점수 업데이트 완료 - articleId: {}", event.getArticleId());
            }
            // 좋아요 취소 시에는 점수를 감소시키지 않음 (또는 별도 로직 구현 가능)
        } catch (Exception e) {
            log.error("게시글 좋아요 점수 업데이트 실패 - articleId: {}", event.getArticleId(), e);
        }
    }
}