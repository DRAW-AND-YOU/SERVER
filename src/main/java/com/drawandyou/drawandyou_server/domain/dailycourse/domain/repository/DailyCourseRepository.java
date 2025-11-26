package com.drawandyou.drawandyou_server.domain.dailycourse.domain.repository;

import com.drawandyou.drawandyou_server.domain.dailycourse.domain.entity.DailyCourse;
import com.drawandyou.drawandyou_server.domain.dailycourse.presentation.dto.DailyCourseScoreResponse;
import com.drawandyou.drawandyou_server.domain.therapyprogram.domain.entity.TherapyProgram;
import com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.dto.response.TherapyProgramScoreResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DailyCourseRepository extends JpaRepository<DailyCourse, Long> {
    int countByTherapyProgramAndIsCompleted(TherapyProgram therapyProgram, boolean isCourseFinished);

    Optional<DailyCourse> findFirstByTherapyProgramAndIsCompletedOrderByCurrentDayAsc(TherapyProgram therapyProgram, boolean b);

    @Query("SELECT new com.drawandyou.drawandyou_server.domain.dailycourse.presentation.dto.DailyCourseScoreResponse(" +
         "dc.id, dc.currentDay, da.totalScore) " +
         "FROM DailyCourse dc " +
         "LEFT JOIN DrawingAnalysis da ON da.drawing.id = dc.drawing.id " +
         "WHERE dc.therapyProgram.id = :therapyProgramId " +
         "ORDER BY dc.currentDay")
    List<DailyCourseScoreResponse> findScoresByTherapyProgramId(Long therapyProgramId);
}
