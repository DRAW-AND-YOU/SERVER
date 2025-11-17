package com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.repository;

import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.request.enums.AnalysisSortType;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.DrawingAnalysisSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DrawingAnalysisRepositoryCustom {

    Page<DrawingAnalysisSimpleResponse> findDrawingAnalysisList(AnalysisSortType sortBy, Long userId, Pageable pageable);
}
