package com.drawandyou.drawandyou_server.domain.article.domain.repository;

import com.drawandyou.drawandyou_server.domain.article.domain.entity.QArticle;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.ArticleResponse;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.PopularArticleResponse;
import com.drawandyou.drawandyou_server.domain.articleimage.domain.entity.QArticleImage;
import com.drawandyou.drawandyou_server.domain.comment.domain.entity.QArticleCommentCount;
import com.drawandyou.drawandyou_server.domain.like.domain.entity.QArticleLikeCount;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QArticle article = QArticle.article;
    private final QArticleImage articleImage = QArticleImage.articleImage;
    private final QUser user = QUser.user;
    private final QArticleLikeCount articleLikeCount = QArticleLikeCount.articleLikeCount;
    private final QArticleCommentCount articleCommentCount = QArticleCommentCount.articleCommentCount;

    @Override
    public List<ArticleResponse> findAllInfiniteScroll(Long userId, Long limit, LocalDateTime lastCreatedAt, Long lastArticleId) {
        return queryFactory
                .select(Projections.constructor(ArticleResponse.class,
                        article.id,
                        articleImage.imageUrl,
                        article.title,
                        user.username,
                        Expressions.constant(0L), // 조회수는 redis 에 저장된 데이터를 사용한다.
                        articleLikeCount.likeCount.coalesce(0L),
                        article.createdAt
                ))
                .from(article)
                .leftJoin(articleImage).on(articleImage.article.eq(article))
                .leftJoin(user).on(article.user.eq(user))
                .leftJoin(articleLikeCount).on(articleLikeCount.articleId.eq(article.id))
                .where(
                        userIdEq(userId),
                        cursorCondition(lastCreatedAt, lastArticleId)
                )
                .orderBy(article.createdAt.desc(), article.id.desc())
                .limit(limit + 1) // hasNext 판단용
                .fetch();
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? article.user.id.eq(userId) : null;
    }

    private BooleanExpression cursorCondition(LocalDateTime lastCreatedAt, Long lastArticleId) {
        if (lastCreatedAt == null || lastArticleId == null) {
            return null;
        }
        return article.createdAt.lt(lastCreatedAt)
                .or(article.createdAt.eq(lastCreatedAt).and(article.id.lt(lastArticleId)));
    }


    @Override
    public List<PopularArticleResponse> findTop10ByIdIn(List<Long> articleIds) {
        if (articleIds == null || articleIds.isEmpty()) {
            return List.of();
        }

        return queryFactory
                .select(Projections.constructor(PopularArticleResponse.class,
                        article.id,
                        articleImage.imageUrl,
                        article.title,
                        user.username,
                        user.profileImageUrl,
                        // view count 는 redis 에서 관리
                        Expressions.constant(0L),
                        articleLikeCount.likeCount.coalesce(0L),
                        articleCommentCount.commentCount.coalesce(0L),
                        article.createdAt
                ))
                .from(article)
                .leftJoin(articleImage).on(articleImage.article.eq(article))
                .leftJoin(user).on(article.user.eq(user))
                .leftJoin(articleLikeCount).on(articleLikeCount.articleId.eq(article.id))
                .leftJoin(articleCommentCount).on(articleCommentCount.articleId.eq(article.id))
                .where(article.id.in(articleIds))
                .fetch();
    }
}