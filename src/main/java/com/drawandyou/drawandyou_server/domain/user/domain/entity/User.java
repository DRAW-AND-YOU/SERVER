package com.drawandyou.drawandyou_server.domain.user.domain.entity;

import com.drawandyou.drawandyou_server.domain.user.domain.entity.enums.Gender;
import com.drawandyou.drawandyou_server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username; // 유저의 이름

    private String email; // 이메일

    private String password; // 비밀번호

    private String role; // 권한

    private String authProvider;

    private String profileImageUrl;

    @Enumerated(value = EnumType.STRING)
    private Gender gender; // 성별

    @ElementCollection
    @CollectionTable(
            name = "user_hobbies",
            joinColumns = @JoinColumn(name = "user_id")
    )
    private List<String> hobbies; // 유저의 관심사 목록

}
