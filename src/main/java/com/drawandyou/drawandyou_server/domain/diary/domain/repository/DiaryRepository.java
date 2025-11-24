package com.drawandyou.drawandyou_server.domain.diary.domain.repository;

import com.drawandyou.drawandyou_server.domain.diary.domain.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long>, DiaryRepositoryCustom {
}
