package com.drawandyou.drawandyou_server.domain.dailycourse.domain.entity;

import com.drawandyou.drawandyou_server.domain.dailycourse.domain.entity.enums.CourseType;
import com.drawandyou.drawandyou_server.domain.drawing.domain.entity.Drawing;
import com.drawandyou.drawandyou_server.global.entity.BaseEntity;
import com.drawandyou.drawandyou_server.domain.therapyprogram.domain.entity.TherapyProgram;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DailyCourse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_course_id")
    private Long id;

    // 데일리 코스와 관련있는 치유 프로그램
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "therapy_program_id")
    private TherapyProgram therapyProgram;

    // 데일리 코스와 연관된 그림 (1개)
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "drawing_id")
    private Drawing drawing;

    // 데일리 코스의 이름 - "1일차 - 집 그리기"
    private String title;

    // 코스가 며칠차 코스인지
    // ex) HTP 테스트라면 1일차겠지.
    private Integer currentDay;

    // 코스에서 진행하는 검사(테스트) 의 종류
    @Enumerated(value = EnumType.STRING)
    private CourseType courseType;

    // 코스 완료 여부
    private boolean isCompleted;

    public void changeStatusToCompleted(){
        this.isCompleted = true;
    }

    public void assignDrawing(Drawing drawing){
        this.drawing = drawing;
    }

}
