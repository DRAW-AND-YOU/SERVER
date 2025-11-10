package com.drawandyou.drawandyou_server.domain.article.domain.entity;

import com.drawandyou.drawandyou_server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ArticleViewCount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_view_count_id")
    private Long id;

    private Long articleId;

    private Long viewCount;

    public static ArticleViewCount init(Long articleId, Long viewCount){
        return ArticleViewCount.builder()
                .articleId(articleId)
                .viewCount(viewCount)
                .build();
    }
}
