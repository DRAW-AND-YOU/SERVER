package com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.repository;

import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.entity.QDrawingAnalysis;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.DrawingAnalysisSimpleResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DrawingAnalysisRepositoryImpl implements DrawingAnalysisRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QDrawingAnalysis drawingAnalysis = QDrawingAnalysis.drawingAnalysis;

    @Override
    public Page<DrawingAnalysisSimpleResponse> findDrawingAnalysisList(Long userId, Pageable pageable) {
        // 전체 개수 조회
        Long total = queryFactory
                .select(drawingAnalysis.count())
                .from(drawingAnalysis)
                .where(drawingAnalysis.drawing.user.id.eq(userId))
                .fetchOne();

        if (total == null) {
            total = 0L;
        }

        // 페이지네이션 결과 조회
        List<DrawingAnalysisSimpleResponse> content = queryFactory
                .select(Projections.constructor(
                        DrawingAnalysisSimpleResponse.class,
                        drawingAnalysis.id,
                        drawingAnalysis.drawing.imageUrl,
                        drawingAnalysis.drawing.title,
                        drawingAnalysis.drawing.createdAt
                ))
                .from(drawingAnalysis)
                .where(drawingAnalysis.drawing.user.id.eq(userId))
                .orderBy(drawingAnalysis.drawing.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }
}