package com.drawandyou.drawandyou_server.domain.like.domain.entity;

import com.drawandyou.drawandyou_server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uk_article_user", columnNames = {"article_id", "user_id"})
})
public class ArticleLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_like_id")
    private Long id;

    private Long articleId;

    private Long userId;

    public static ArticleLike create(Long articleId, Long userId) {
        return ArticleLike.builder()
                .articleId(articleId)
                .userId(userId)
                .build();
    }








}
