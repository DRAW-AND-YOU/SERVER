package com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.entity;

import com.drawandyou.drawandyou_server.domain.drawing.domain.entity.Drawing;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DrawingAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "drawing_id")
    private Drawing drawing;

    // 분석 점수
    private int score;

    // 색상 분석 결과
    private String colorAnalysis;

    // 구도 분석 결과
    private String compositionAnalysis;

    // 선 분석 결과
    private String lineAnalysis;

    // 감정 상태
    private String emotionStatus;

}
