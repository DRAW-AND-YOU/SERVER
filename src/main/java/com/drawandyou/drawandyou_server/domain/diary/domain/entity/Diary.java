package com.drawandyou.drawandyou_server.domain.diary.domain.entity;

import com.drawandyou.drawandyou_server.domain.diary.domain.entity.enums.EmotionKeyword;
import com.drawandyou.drawandyou_server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "idx_diary_author_written", columnList = "author_id,written_at")
})
public class Diary extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    // 일기 작성자 id
    private Long authorId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    // 일기 이미지 URL
    private String imageUrl;

    // 일기 작성 시간
    private LocalDateTime writtenAt;

    // 일기 작성 시점에서, 입력하는 감정 키워드
    @Enumerated(value = EnumType.STRING)
    private EmotionKeyword emotionKeyword;

    public static Diary create(Long authorId, String title, String content,  String imageUrl, LocalDateTime writtenAt, EmotionKeyword emotionKeyword) {
        return Diary.builder()
                .authorId(authorId)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .writtenAt(writtenAt)
                .emotionKeyword(emotionKeyword)
                .build();
    }

    public void update(String title, String content, EmotionKeyword emotionKeyword) {
        this.title = title;
        this.content = content;
        this.emotionKeyword = emotionKeyword;
    }

}
