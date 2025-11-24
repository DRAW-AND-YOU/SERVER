package com.drawandyou.drawandyou_server.domain.diary.domain.repository;

import com.drawandyou.drawandyou_server.domain.diary.domain.entity.QDiary;
import com.drawandyou.drawandyou_server.domain.diary.presentation.dto.response.DiaryCalendarResponse;
import com.drawandyou.drawandyou_server.domain.diary.presentation.dto.response.DiarySimpleResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DiaryRepositoryImpl implements DiaryRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final QDiary diary = QDiary.diary;

    @Override
    public DiaryCalendarResponse findDiariesForCalendar(Long userId, LocalDate startDate, LocalDate endDate) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<DiarySimpleResponse> results = queryFactory
                .select(Projections.constructor(DiarySimpleResponse.class,
                        diary.id,
                        diary.writtenAt,
                        diary.imageUrl
                ))
                .from(diary)
                .where(diary.authorId.eq(userId)
                        .and(diary.writtenAt.goe(startDateTime))
                        .and(diary.writtenAt.loe(endDateTime)))
                .fetch();

        return new DiaryCalendarResponse(results);
    }
}
