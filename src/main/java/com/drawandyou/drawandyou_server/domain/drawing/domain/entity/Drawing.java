package com.drawandyou.drawandyou_server.domain.drawing.domain.entity;

import com.drawandyou.drawandyou_server.global.entity.BaseEntity;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Drawing extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drawing_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 그림을 그린 유저

    private String title; // 그림 제목

    private String imageUrl; // 이미지 url

    private LocalDateTime drawnAt; // 그림을 그린 시간

    // 분석 결과 관련 필드를 추가해야한다.

    public static Drawing createDrawing(User user, String title, String imageUrl){
        return Drawing.builder()
                .user(user)
                .title(title)
                .imageUrl(imageUrl)
                .drawnAt(LocalDateTime.now())
                .build();
    }

}
