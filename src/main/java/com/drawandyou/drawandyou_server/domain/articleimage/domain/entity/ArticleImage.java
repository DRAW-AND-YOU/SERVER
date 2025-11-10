package com.drawandyou.drawandyou_server.domain.articleimage.domain.entity;

import com.drawandyou.drawandyou_server.domain.article.domain.entity.Article;
import com.drawandyou.drawandyou_server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ArticleImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_image_id")
    private Long id;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    private String imageUrl;

    public static ArticleImage create(Article article, String imageUrl){
        return ArticleImage.builder()
                .article(article)
                .imageUrl(imageUrl)
                .build();
    }

}
