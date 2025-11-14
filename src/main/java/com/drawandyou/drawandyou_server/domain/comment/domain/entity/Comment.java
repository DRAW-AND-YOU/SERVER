package com.drawandyou.drawandyou_server.domain.comment.domain.entity;

import com.drawandyou.drawandyou_server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    private Long articleId;

    private Long userId;

    @Embedded
    private CommentPath commentPath;

    private Boolean deleted;

    public static Comment create(String content, Long articleId, Long userId, CommentPath commentPath){
        return Comment.builder()
                .content(content)
                .articleId(articleId)
                .userId(userId)
                .commentPath(commentPath)
                .deleted(false)
                .build();
    }

    public boolean isRoot() {
        return commentPath.isRoot();
    }

    public void delete() {
        deleted = true;
    }

}
