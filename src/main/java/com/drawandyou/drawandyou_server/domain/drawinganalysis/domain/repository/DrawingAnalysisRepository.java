package com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.repository;

import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.entity.DrawingAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrawingAnalysisRepository extends JpaRepository<DrawingAnalysis, Long> {
}
