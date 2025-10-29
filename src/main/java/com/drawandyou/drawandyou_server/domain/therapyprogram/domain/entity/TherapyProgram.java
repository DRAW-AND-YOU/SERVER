package com.drawandyou.drawandyou_server.domain.therapyprogram.domain.entity;

import com.drawandyou.drawandyou_server.global.entity.BaseEntity;
import com.drawandyou.drawandyou_server.domain.therapyprogram.domain.entity.enums.ProgramStatus;
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
public class TherapyProgram extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 치유 프로그램을 진행중인 유저
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 유저가 치유 프로그램 시작한 날짜
    private LocalDateTime startDate;

    // 유저가 치유 프로그램을 마친 날짜
    private LocalDateTime endDate;

    // 치유 프로그램 자체의 상태
    @Enumerated(value = EnumType.STRING)
    private ProgramStatus programStatus;

    // 치유 프로그램의 현재 진행 일차(날짜가 넘어가면, 하루 증가시켜야한다.)
    private Integer currentDay;

    // 치유 프로그램의 전체 일수 (7일로 고정이긴하다.)
    private Integer totalDays = 7;
}
