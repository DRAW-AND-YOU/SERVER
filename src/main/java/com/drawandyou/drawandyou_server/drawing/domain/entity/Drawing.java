package com.drawandyou.drawandyou_server.drawing.domain.entity;

import com.drawandyou.drawandyou_server.global.entity.BaseEntity;
import com.drawandyou.drawandyou_server.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Drawing extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String imageUrl;

    // 분석 결과 관련 필드를 추가해야한다.

    public static Drawing createDrawing(User user, String imageUrl){
        return Drawing.builder()
                .user(user)
                .imageUrl(imageUrl)
                .build();
    }

}
