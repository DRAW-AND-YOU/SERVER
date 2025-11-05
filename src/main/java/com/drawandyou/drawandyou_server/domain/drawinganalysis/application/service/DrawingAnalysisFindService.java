package com.drawandyou.drawandyou_server.domain.drawinganalysis.application.service;

import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.entity.DrawingAnalysis;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.domain.repository.DrawingAnalysisRepository;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.exception.DrawingAnalysisNotFoundException;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.exception.DrawingAnalysisViewException;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.DrawingAnalysisDetailResponse;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.DrawingAnalysisListResponse;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.DrawingAnalysisSimpleResponse;
import com.drawandyou.drawandyou_server.domain.user.application.service.UserFindService;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DrawingAnalysisFindService {

    private final DrawingAnalysisRepository drawingAnalysisRepository;
    private final UserFindService userFindService;

    public DrawingAnalysisDetailResponse getDrawingAnalysisDetail(Long userId, Long drawingAnalysisId) {
        User user = userFindService.findUser(userId);

        DrawingAnalysis drawingAnalysis = drawingAnalysisRepository.findById(drawingAnalysisId)
                .orElseThrow(DrawingAnalysisNotFoundException::new);

        // 마이페이지에서의 조회이기 때문에, 해당 그림을 그린 소유자가 아니라면 조회 불가능
        if (!drawingAnalysis.getDrawing().getUser().getId().equals(user.getId())){
            throw new DrawingAnalysisViewException();
        }
        Hibernate.initialize(drawingAnalysis.getVideoRecommendations());
        Hibernate.initialize(drawingAnalysis.getMusicRecommendations());
        Hibernate.initialize(drawingAnalysis.getPlaceRecommendations());

        return DrawingAnalysisDetailResponse.toResponse(drawingAnalysis);
    }

    public DrawingAnalysisListResponse getDrawingAnalysisList(Long userId, int page, int size) {
        Page<DrawingAnalysisSimpleResponse> results = drawingAnalysisRepository.findDrawingAnalysisList(userId, PageRequest.of(page, size));
        return DrawingAnalysisListResponse.of(results);
    }
}
