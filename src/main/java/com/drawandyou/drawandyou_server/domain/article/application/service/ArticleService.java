package com.drawandyou.drawandyou_server.domain.article.application.service;

import com.drawandyou.drawandyou_server.domain.article.domain.entity.Article;
import com.drawandyou.drawandyou_server.domain.article.exception.ArticleCanNotDeleteException;
import com.drawandyou.drawandyou_server.domain.article.exception.ArticleNotFoundException;
import com.drawandyou.drawandyou_server.domain.article.exception.ArticleNotModifiableException;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.request.ArticleCreateRequest;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.request.ArticleUpdateRequest;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.ArticleCreateResponse;
import com.drawandyou.drawandyou_server.domain.article.domain.repository.ArticleRepository;

import com.drawandyou.drawandyou_server.domain.articleimage.domain.entity.ArticleImage;
import com.drawandyou.drawandyou_server.domain.articleimage.domain.repository.ArticleImageRepository;
import com.drawandyou.drawandyou_server.domain.user.application.service.UserFindService;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;

    private final UserFindService userFindService;

    @Transactional
    public ArticleCreateResponse createArticle(Long userId, ArticleCreateRequest request){

        User user = userFindService.findUser(userId);
        Article article = Article.create(user, request.title(), request.content());

        Article savedArticle = articleRepository.save(article);

        String articleImageUrl = request.imageUrl();
        articleImageRepository.save(ArticleImage.create(savedArticle, articleImageUrl));

        return ArticleCreateResponse.toResponse(userId, savedArticle.getId(), articleImageUrl);
    }


    @Transactional
    public void updateArticle(Long userId, Long articleId, ArticleUpdateRequest request) {

        User user = userFindService.findUser(userId);
        Article article = articleRepository.findById(articleId)
                .orElseThrow(ArticleNotFoundException::new);

        // user 가 article 의 소유자인지 검증한다.
        if (!article.getUser().equals(user)){
            throw new ArticleNotModifiableException();
        }
        // 검증 성공시 수정 가능
        article.updateTitleAndContent(request.title(), request.content());

        // 이미지도 update
        // 이미지 수정 요청이 존재한다면 이미지도 갈아끼우기
        if (request.imageUrl() != null){
            updateArticleImage(request.imageUrl(), article);
        }
    }

    @Transactional
    public void deleteArticle(Long userId, Long articleId) {

        User user = userFindService.findUser(userId);
        Article article = articleRepository.findById(articleId)
                .orElseThrow(ArticleNotFoundException::new);

        // 게시물을 삭제할 수 있는 권한이 있는지 검증 , 권한이 없다면 예외 발생
        if (!article.getUser().equals(user)){
            throw new ArticleCanNotDeleteException();
        }

        // 게시글과 관련된 이미지 먼저 삭제 (외래키 제약조건)
        articleImageRepository.deleteAllByArticle(article);

        // 이후, 게시글 삭제
        articleRepository.delete(article);
    }

    private void updateArticleImage(String toUpdateImageUrl, Article article) {
        ArticleImage articleImage = articleImageRepository.findByArticle(article);
        articleImageRepository.delete(articleImage);
        articleImageRepository.save(ArticleImage.create(article , toUpdateImageUrl));
    }


}
