package com.drawandyou.drawandyou_server.domain.user.domain.entity;

import com.drawandyou.drawandyou_server.domain.user.domain.entity.enums.Gender;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.enums.Hobby;
import com.drawandyou.drawandyou_server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    private String nickname;

    private String email; // 이메일

    private String password; // 비밀번호

    private String role; // 권한

    private String authProvider;

    private String profileImageUrl;

    private LocalDate birthDate; // 생년월일

    @Enumerated(value = EnumType.STRING)
    private Gender gender; // 성별

    @ElementCollection
    @CollectionTable(
            name = "user_hobbies",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(value = EnumType.STRING)
    private List<Hobby> hobbies; // 유저의 관심사 목록

    public void assignNickname(String nickname){
        this.nickname = nickname;
    }

    public void assignBirthDate(LocalDate birthDate){
        this.birthDate = birthDate;
    }

    public void assignGender(Gender gender){
        this.gender = gender;
    }

    public void assignHobbies(List<Hobby> hobbies){
        this.hobbies = hobbies;
    }

    public void changeProfileImage(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

    public void changePassword(String newPassword){
        this.password = newPassword;
    }



}
