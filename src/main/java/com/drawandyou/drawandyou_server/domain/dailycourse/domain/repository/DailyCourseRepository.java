package com.drawandyou.drawandyou_server.domain.dailycourse.domain.repository;

import com.drawandyou.drawandyou_server.domain.dailycourse.domain.entity.DailyCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyCourseRepository extends JpaRepository<DailyCourse, Long> {
}
