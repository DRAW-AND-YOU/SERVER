package com.drawandyou.drawandyou_server.domain.diary.domain.repository;

import com.drawandyou.drawandyou_server.domain.diary.domain.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long>, DiaryRepositoryCustom {

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
            "FROM Diary d WHERE d.authorId = :userId " +
            "AND d.writtenAt >= :start AND d.writtenAt < :end")
    boolean existsByUserIdAndWrittenAtBetween(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

}
