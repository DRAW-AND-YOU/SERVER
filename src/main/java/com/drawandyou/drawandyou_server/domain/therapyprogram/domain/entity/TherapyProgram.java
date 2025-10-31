package com.drawandyou.drawandyou_server.domain.therapyprogram.domain.entity;

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
public class TherapyProgram extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "therapy_program_id")
    private Long id;

    // 치유 프로그램을 진행중인 유저
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 유저가 치유 프로그램 시작한 날짜
    private LocalDateTime startDate;

    // 유저가 치유 프로그램을 마친 날짜
    private LocalDateTime endDate;

    // 치유 프로그램 종료 여부(유저가 치유 프로그램의 코스를 모두 진행하면 완료처리)
    private boolean isFinished;

    // 치유 프로그램의 전체 일수
    @Builder.Default
    private Integer totalDays = 5;
}
