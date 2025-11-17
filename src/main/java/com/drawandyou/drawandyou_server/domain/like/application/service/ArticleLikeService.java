package com.drawandyou.drawandyou_server.domain.like.application.service;

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
        articleLikeRepository.save(ArticleLike.create(articleId, userId));

        // update 할 데이터가 없을 수도 있다.
        int result = articleLikeCountRepository.increase(articleId);
        if (result == 0){ // 아직 data 가 없는 거니까, 초기화해서 넣어주면 된다.
            // 즉 최초 요청시에는 update 되는 레코드가 없으므로, 1로 초기화.
            // 트래픽이 순식간에 몰리는 상황에서는 유실 가능성이 있으므로, 게시글 생성 시점에 0으로 초기화 해둘 수도 있겠지.
            articleLikeCountRepository.save(
                    ArticleLikeCount.init(articleId, 1L)
            );
        }
    }

    @Transactional
    public void unlikePessimisticLock(Long articleId, Long userId){
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLike -> {
                    articleLikeRepository.delete(articleLike);
                    articleLikeCountRepository.decrease(articleId);
                });
    }
}
