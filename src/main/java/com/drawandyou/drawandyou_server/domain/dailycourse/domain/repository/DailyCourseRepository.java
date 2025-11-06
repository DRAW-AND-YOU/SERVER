package com.drawandyou.drawandyou_server.domain.dailycourse.domain.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.drawandyou.drawandyou_server.domain.dailycourse.domain.entity.DailyCourse;
import com.drawandyou.drawandyou_server.domain.therapyprogram.domain.entity.TherapyProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DailyCourseRepository extends JpaRepository<DailyCourse, Long> {
    int countByTherapyProgramAndIsCompleted(TherapyProgram therapyProgram, boolean isCourseFinished);

    Optional<DailyCourse> findFirstByTherapyProgramAndIsCompletedOrderByCurrentDayAsc(TherapyProgram therapyProgram, boolean b);
}
