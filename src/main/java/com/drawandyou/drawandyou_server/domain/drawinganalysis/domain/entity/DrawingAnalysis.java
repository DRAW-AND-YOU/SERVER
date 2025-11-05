package com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.entity;

import com.drawandyou.drawandyou_server.domain.drawing.domain.entity.Drawing;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.vo.MusicRecommendationValue;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.vo.PlaceRecommendationValue;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.vo.VideoRecommendationValue;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DrawingAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drawing_analysis_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "drawing_id")
    private Drawing drawing;

    // 색상 분석 결과
    @Column(columnDefinition = "TEXT")
    private String colorAnalysis;

    // 구도 분석 결과
    @Column(columnDefinition = "TEXT")
    private String compositionAnalysis;

    // 선 분석 결과
    @Column(columnDefinition = "TEXT")
    private String lineAnalysis;

    // 감정 상태
    private String emotionStatus;

    // 분석 점수
    private Integer totalScore;

    // 세부 점수 - 객체 점수
    private Integer objectScore;

    // 세부 점수 - 이미지 점수
    private Integer imageScore;

    // 세부 점수 - 후질문 점수
    private Integer questionScore;

    // AI 추천 결과 - 음악
    @ElementCollection
    @CollectionTable(
            name = "music_recommendations",
            joinColumns = @JoinColumn(name = "drawing_analysis_id")
    )
    private List<MusicRecommendationValue> musicRecommendations = new ArrayList<>();

    // AI 추천 결과 - 비디오
    @ElementCollection
    @CollectionTable(
            name = "video_recommendations",
            joinColumns = @JoinColumn(name = "drawing_analysis_id")
    )
    private List<VideoRecommendationValue> videoRecommendations = new ArrayList<>();

    // AI 추천 결과 - 장소
    @ElementCollection
    @CollectionTable(
            name = "place_recommendations",
            joinColumns = @JoinColumn(name = "drawing_analysis_id")
    )
    private List<PlaceRecommendationValue> placeRecommendations = new ArrayList<>();

    /**
     * DrawingAnalysisResponse를 기반으로 DrawingAnalysis 엔티티를 생성
     */
    public static DrawingAnalysis createAnalysis(
            Drawing drawing,
            String colorAnalysis,
            String compositionAnalysis,
            String lineAnalysis,
            String emotionStatus,
            Integer totalScore,
            Integer objectScore,
            Integer imageScore,
            Integer questionScore
    ) {
        return DrawingAnalysis.builder()
                .drawing(drawing)
                .colorAnalysis(colorAnalysis)
                .compositionAnalysis(compositionAnalysis)
                .lineAnalysis(lineAnalysis)
                .emotionStatus(emotionStatus)
                .totalScore(totalScore)
                .objectScore(objectScore)
                .imageScore(imageScore)
                .questionScore(questionScore)
                .musicRecommendations(new ArrayList<>())
                .videoRecommendations(new ArrayList<>())
                .placeRecommendations(new ArrayList<>())
                .build();
    }

    /**
     * AI 추천 결과를 할당.
     */
    public void addRecommendations(
            List<MusicRecommendationValue> musicRecommendations,
            List<VideoRecommendationValue> videoRecommendations,
            List<PlaceRecommendationValue> placeRecommendations
    ) {
        this.musicRecommendations.clear();
        this.musicRecommendations.addAll(musicRecommendations);

        this.videoRecommendations.clear();
        this.videoRecommendations.addAll(videoRecommendations);

        this.placeRecommendations.clear();
        this.placeRecommendations.addAll(placeRecommendations);
    }
}
