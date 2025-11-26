package com.drawandyou.drawandyou_server.domain.therapyprogram.domain.repository;

import com.drawandyou.drawandyou_server.domain.therapyprogram.domain.entity.TherapyProgram;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TherapyProgramRepository extends JpaRepository<TherapyProgram, Long> {

    boolean existsByUserId(Long userId);

    Optional<TherapyProgram> findByUserAndIsFinished(User user, boolean isFinished);

    boolean existsByUserAndIsFinished(User user, boolean isFinished);

    List<TherapyProgram> findTherapyProgramsByUserAndIsFinished(User user, boolean isFinished);
}
