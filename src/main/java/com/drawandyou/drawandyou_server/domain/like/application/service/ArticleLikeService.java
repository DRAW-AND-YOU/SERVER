package com.drawandyou.drawandyou_server.domain.like.application.service;

import com.drawandyou.drawandyou_server.domain.comment.domain.entity.ArticleCommentCount;
import com.drawandyou.drawandyou_server.domain.like.domain.entity.ArticleLike;
import com.drawandyou.drawandyou_server.domain.like.domain.entity.ArticleLikeCount;
import com.drawandyou.drawandyou_server.domain.like.domain.repository.ArticleLikeCountRepository;
import com.drawandyou.drawandyou_server.domain.like.domain.repository.ArticleLikeRepository;
import com.drawandyou.drawandyou_server.domain.like.exception.ArticleLikeNotFoundException;
import com.drawandyou.drawandyou_server.domain.like.presentation.dto.response.ArticleLikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;

    public ArticleLikeResponse read(Long articleId, Long userId) {
        return articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .map(ArticleLikeResponse::from)
                .orElseThrow(ArticleLikeNotFoundException::new);
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
    }

    @Transactional
    public void unlikePessimisticLock(Long articleId, Long userId){
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLike -> {
                    articleLikeRepository.delete(articleLike);
                    articleLikeCountRepository.decrease(articleId);
                });
    }

    public Long count(Long articleId){
        return articleLikeCountRepository.findByArticleId(articleId)
                .map(ArticleLikeCount::getLikeCount)
                .orElse(0L);
    }
}
