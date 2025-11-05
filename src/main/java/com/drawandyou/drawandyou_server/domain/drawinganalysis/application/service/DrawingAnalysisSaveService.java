package com.drawandyou.drawandyou_server.domain.drawinganalysis.application.service;

import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.entity.DrawingAnalysis;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.repository.DrawingAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DrawingAnalysisSaveService {

    private final DrawingAnalysisRepository drawingAnalysisRepository;

    public DrawingAnalysis save(DrawingAnalysis drawingAnalysis) {
        return drawingAnalysisRepository.save(drawingAnalysis);
    }
}
