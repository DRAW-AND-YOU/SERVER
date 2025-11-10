package com.drawandyou.drawandyou_server.domain.article.domain.entity;

import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import com.drawandyou.drawandyou_server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String content;

    public static Article create(User user, String title, String content){
        return Article.builder()
                .user(user)
                .title(title)
                .content(content)
                .build();
    }

    public void updateTitleAndContent(String title, String content){
        if (title != null && !title.isBlank()){
            this.title = title;
        }
        if (content != null && !content.isBlank()){
            this.content = content;
        }
    }
}
