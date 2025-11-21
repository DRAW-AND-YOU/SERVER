package com.drawandyou.drawandyou_server.domain.article.application.service;

import com.drawandyou.drawandyou_server.domain.article.domain.entity.Article;
import com.drawandyou.drawandyou_server.domain.article.domain.event.ArticleViewEvent;
import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleRepository;
import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleViewCountRepository;
import com.drawandyou.drawandyou_server.domain.article.exception.ArticleNotFoundException;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.ArticleDetailResponse;
import com.drawandyou.drawandyou_server.domain.articleimage.domain.entity.ArticleImage;
import com.drawandyou.drawandyou_server.domain.articleimage.domain.repository.ArticleImageRepository;
import com.drawandyou.drawandyou_server.domain.like.domain.entity.ArticleLikeCount;
import com.drawandyou.drawandyou_server.domain.like.domain.repository.ArticleLikeCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleDetailService {

    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final ArticleViewService articleViewService;

    /**
     * 게시글 상세 조회
     * 조회 시 조회수를 증가시키고, 인기 점수를 업데이트 하는 이벤트를 발행
     */
    @Transactional(readOnly = true)
    public ArticleDetailResponse getArticleDetail(Long articleId, Long userId) {

        // 게시글 조회
        Article article = articleRepository.findById(articleId)
                .orElseThrow(ArticleNotFoundException::new);

        Long viewCount = articleViewService.increase(articleId, userId);

        // 조회 이벤트 발행
        publishViewEvent(articleId, userId);

        Long likeCount = articleLikeCountRepository.findById(articleId)
                .map(ArticleLikeCount::getLikeCount)
                .orElse(0L);

        ArticleImage articleImage = articleImageRepository.findByArticle(article);

        return ArticleDetailResponse.toResponse(
                article,
                articleImage,
                viewCount,
                likeCount
        );
    }

    /**
     * 조회 이벤트 발행
     */
    private void publishViewEvent(Long articleId, Long userId) {
        try {
            ArticleViewEvent event = ArticleViewEvent.of(articleId, userId);
            eventPublisher.publishEvent(event);

            log.debug("게시글 조회 이벤트 발행 - articleId: {}, userId: {}", articleId, userId);
        } catch (Exception e) {
            // 이벤트 발행 실패해도 조회는 정상 처리
            log.error("조회 이벤트 발행 실패 - articleId: {}", articleId, e);
        }
    }
}