package com.drawandyou.drawandyou_server.domain.like.domain.entity;

import com.drawandyou.drawandyou_server.domain.article.domain.entity.ArticleViewCount;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ArticleLikeCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_like_count_id")
    private Long id;

    private Long articleId;

    private Long likeCount;

    @Version
    private Long version;

    public static ArticleLikeCount init(Long articleId, Long likeCount){
        return ArticleLikeCount.builder()
                .articleId(articleId)
                .likeCount(likeCount)
                .build();
    }

    public void increase(){
        this.likeCount++;
    }

    public void decrease(){
        this.likeCount--;
    }





}
