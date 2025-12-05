package com.drawandyou.drawandyou_server.domain.article.application.service;

import com.drawandyou.drawandyou_server.domain.article.domain.entity.Article;
import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleCommentCountRepository;
import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleViewCountRepository;
import com.drawandyou.drawandyou_server.domain.article.exception.ArticleCanNotDeleteException;
import com.drawandyou.drawandyou_server.domain.article.exception.ArticleNotModifiableException;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.request.ArticleCreateRequest;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.request.ArticleUpdateRequest;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.ArticleCreateResponse;
import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleRepository;

import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.ArticleResponse;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.ArticleScrollResponse;
import com.drawandyou.drawandyou_server.domain.articleimage.domain.entity.ArticleImage;
import com.drawandyou.drawandyou_server.domain.articleimage.domain.repository.ArticleImageRepository;
import com.drawandyou.drawandyou_server.domain.comment.domain.entity.ArticleCommentCount;
import com.drawandyou.drawandyou_server.domain.like.domain.entity.ArticleLikeCount;
import com.drawandyou.drawandyou_server.domain.like.domain.repository.ArticleLikeCountRepository;
import com.drawandyou.drawandyou_server.domain.user.application.service.UserFindService;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;

    private final ArticleCommentCountRepository articleCommentCountRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;
    private final ArticleViewCountRepository articleViewCountRepository;

    private final UserFindService userFindService;
    private final ArticleFindService articleFindService;

    @Transactional
    public ArticleCreateResponse createArticle(Long userId, ArticleCreateRequest request){

        User user = userFindService.findUser(userId);
        Article article = Article.create(user, request.title(), request.content());

        Article savedArticle = articleRepository.save(article);

        String articleImageUrl = request.imageUrl();
        articleImageRepository.save(ArticleImage.create(savedArticle, articleImageUrl));

        // article 생성시점에 , article comment count , article like count 를 0으로 초기화하자.
        articleLikeCountRepository.save(ArticleLikeCount.init(savedArticle.getId(), 0L));
        articleCommentCountRepository.save(ArticleCommentCount.init(savedArticle.getId(), 0L));

        return ArticleCreateResponse.toResponse(userId, savedArticle.getId(), articleImageUrl);
    }


    @Transactional
    public void updateArticle(Long userId, Long articleId, ArticleUpdateRequest request) {

        User user = userFindService.findUser(userId);
        Article article = articleFindService.findById(articleId);

        // user 가 article 의 소유자인지 검증한다.
        if (!article.getUser().equals(user)){
            throw new ArticleNotModifiableException();
        }
        // 검증 성공시 수정 가능
        article.updateTitleAndContent(request.title(), request.content());
    }

    @Transactional
    public void deleteArticle(Long userId, Long articleId) {

        User user = userFindService.findUser(userId);
        Article article = articleFindService.findById(articleId);

        // 게시물을 삭제할 수 있는 권한이 있는지 검증 , 권한이 없다면 예외 발생
        if (!article.getUser().equals(user)){
            throw new ArticleCanNotDeleteException();
        }

        // 게시글과 관련된 이미지 먼저 삭제 (외래키 제약조건)
        articleImageRepository.deleteAllByArticle(article);

        // 이후, 게시글 삭제
        articleRepository.delete(article);
    }

    @Transactional(readOnly = true)
    public ArticleScrollResponse readAllInfiniteScroll(Long userId, LocalDateTime lastCreatedAt, Long pageSize, Long lastArticleId){

        List<ArticleResponse> articles = articleRepository.findAllInfiniteScroll(userId, pageSize, lastCreatedAt, lastArticleId);

        // redis 에서 multi get 으로 articleIds 에 대한 조회수를 한번에 조회한다.
        List<Long> articleIds = articles.stream()
                .map(ArticleResponse::articleId)
                .toList();

        Map<Long, Long> viewCountMap = articleViewCountRepository.readMultiple(articleIds);

        // 조회수를 포함해서 response 다시 조립하기
        List<ArticleResponse> articlesWithViewCount = articles.stream()
                .map(article -> new ArticleResponse(
                        article.articleId(),
                        article.imageUrl(),
                        article.title(),
                        article.authorName(),
                        viewCountMap.getOrDefault(article.articleId(), 0L),
                        article.likeCount(),
                        article.createdAt()
                ))
                .toList();

        // ArticleScrollResponse로 변환하기
        return ArticleScrollResponse.of(articlesWithViewCount, pageSize);
    }

    // 유저가 작성한 게시글 수 반환
    public Long getArticleCount(Long userId) {
        return articleRepository.countByUserId(userId);
    }

}
