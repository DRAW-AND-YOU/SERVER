package com.drawandyou.drawandyou_server.domain.drawing.application.service;

import com.drawandyou.drawandyou_server.domain.drawing.domain.entity.Drawing;
import com.drawandyou.drawandyou_server.domain.drawing.domain.repository.DrawingRepository;
import com.drawandyou.drawandyou_server.domain.drawing.presentation.dto.request.DrawingSaveRequest;
import com.drawandyou.drawandyou_server.domain.drawing.presentation.dto.response.DrawingAnalysisResponse;
import com.drawandyou.drawandyou_server.domain.user.application.service.UserFindService;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DrawingSaveService {

    private final DrawingRepository drawingRepository;

    private final UserFindService userFindService;

    public DrawingAnalysisResponse saveAndAnalyzeDrawing(Long userId, DrawingSaveRequest drawingSaveRequest) {
        User user = userFindService.findUser(userId);
        String imageUrl = drawingSaveRequest.imageUrl();

        Drawing drawing = Drawing.createDrawing(user, imageUrl);
        drawingRepository.save(drawing);

        // fast api 이미지 분석 비동기 호출
        return new DrawingAnalysisResponse("임시 값");
    }
}
