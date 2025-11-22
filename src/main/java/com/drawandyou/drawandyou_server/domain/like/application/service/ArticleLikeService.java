package com.drawandyou.drawandyou_server.domain.like.application.service;

import com.drawandyou.drawandyou_server.domain.like.domain.entity.ArticleLike;
import com.drawandyou.drawandyou_server.domain.like.domain.entity.ArticleLikeCount;
import com.drawandyou.drawandyou_server.domain.like.domain.event.ArticleLikeEvent;
import com.drawandyou.drawandyou_server.domain.like.domain.repository.ArticleLikeCountRepository;
import com.drawandyou.drawandyou_server.domain.like.domain.repository.ArticleLikeRepository;
import com.drawandyou.drawandyou_server.domain.like.presentation.dto.response.ArticleLikeResponse;
import com.drawandyou.drawandyou_server.domain.like.presentation.dto.response.LikeCountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ArticleLikeResponse read(Long articleId, Long userId) {
        boolean isLiked = articleLikeRepository.existsByArticleIdAndUserId(articleId, userId);
        return ArticleLikeResponse.of(userId, isLiked);
    }

    // update 시점에만 쓰기 락 잡기
    @Transactional
    public void likePessimisticLock(Long articleId, Long userId){

        // 좋아요를 누를때, 이미 존재하면 return
        if (articleLikeRepository.findByArticleIdAndUserId(articleId, userId).isPresent()){
            return;
        }
        articleLikeRepository.save(ArticleLike.create(articleId, userId));

        articleLikeCountRepository.increase(articleId);
        // 좋아요 이벤트 발행
        publishLikeEvent(articleId, userId, true);
    }

    @Transactional
    public void unlikePessimisticLock(Long articleId, Long userId){
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLike -> {
                    articleLikeRepository.delete(articleLike);
                    articleLikeCountRepository.decrease(articleId);

                    // 좋아요 취소 이벤트 발행
                    publishLikeEvent(articleId, userId, false);
                });
    }

    public LikeCountResponse count(Long articleId){
        Long likeCount = articleLikeCountRepository.findByArticleId(articleId)
                .map(ArticleLikeCount::getLikeCount)
                .orElse(0L);

        return new LikeCountResponse(likeCount);
    }

    /**
     * 좋아요/좋아요 취소 이벤트 발행
     */
    private void publishLikeEvent(Long articleId, Long userId, boolean liked) {
        try {
            ArticleLikeEvent event = liked
                ? ArticleLikeEvent.ofLike(articleId, userId)
                : ArticleLikeEvent.ofUnlike(articleId, userId);

            eventPublisher.publishEvent(event);
            log.debug("좋아요 이벤트 발행 - articleId: {}, userId: {}, liked: {}", articleId, userId, liked);
        } catch (Exception e) {
            log.error("좋아요 이벤트 발행 실패 - articleId: {}, userId: {}", articleId, userId, e);
        }
    }
}
