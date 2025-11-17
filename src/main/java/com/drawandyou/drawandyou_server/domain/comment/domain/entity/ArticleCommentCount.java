package com.drawandyou.drawandyou_server.domain.comment.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ArticleCommentCount {

    @Id
    @Column(name = "article_id")
    private Long articleId;

    private Long commentCount;

    public static ArticleCommentCount init(Long articleId, Long commentCount) {
        return ArticleCommentCount.builder()
                .articleId(articleId)
                .commentCount(commentCount)
                .build();
    }
}
