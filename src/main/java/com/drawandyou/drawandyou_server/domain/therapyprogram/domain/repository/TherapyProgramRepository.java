package com.drawandyou.drawandyou_server.domain.therapyprogram.domain.repository;

import com.drawandyou.drawandyou_server.domain.therapyprogram.domain.entity.TherapyProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TherapyProgramRepository extends JpaRepository<TherapyProgram, Long> {
}
