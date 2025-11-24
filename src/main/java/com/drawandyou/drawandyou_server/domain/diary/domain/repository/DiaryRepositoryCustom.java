package com.drawandyou.drawandyou_server.domain.diary.domain.repository;

import com.drawandyou.drawandyou_server.domain.diary.presentation.dto.response.DiaryCalendarResponse;

import java.time.LocalDate;

public interface DiaryRepositoryCustom {
    DiaryCalendarResponse findDiariesForCalendar(Long userId, LocalDate startDate, LocalDate endDate);
}
