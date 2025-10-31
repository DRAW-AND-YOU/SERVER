package com.drawandyou.drawandyou_server.domain.drawinganalysis.application.service;

import com.drawandyou.drawandyou_server.domain.drawing.application.service.DrawingSaveService;
import com.drawandyou.drawandyou_server.domain.drawing.domain.entity.Drawing;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.request.DrawingSaveRequest;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.DrawingAnalysisResponse;
import com.drawandyou.drawandyou_server.domain.user.application.service.UserFindService;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import com.drawandyou.drawandyou_server.global.client.fastapi.FastApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DrawingAnalyzeService {

    private final DrawingSaveService drawingSaveService;
    private final DrawingAnalysisSaveService drawingAnalysisSaveService;
    private final UserFindService userFindService;

    private final FastApiClient fastApiClient;

    public DrawingAnalysisResponse saveAndAnalyzeDrawing(Long userId, DrawingSaveRequest drawingSaveRequest) {
        User user = userFindService.findUser(userId);
        String imageUrl = drawingSaveRequest.imageUrl();
        String title = drawingSaveRequest.title();

        Drawing drawing = Drawing.createDrawing(user, title, imageUrl);
        Drawing savedDrawing = drawingSaveService.save(drawing); // 그림 저장 완료

//       fast api 이미지 분석 비동기 호출
//       분석 결과 DTO =  getDrawingAnalysisResultFromFastApi(drawing.getImageUrl(), 각종 추가 정보들...)

//        DrawingAnalysis.createAnalysis(분석 결과 DTO); 분석 결과 응답을 바탕으로, DrawingAnalysis 조립하기

//        분석 결과 저장
//        drawingAnalysisSaveService.save(분석 결과 엔티티);

        return new DrawingAnalysisResponse("임시 값"); // 클라이언트에 분석 결과 반환
    }

//    public 응답dto getDrawingAnalysisResultFromFastApi(String drawingImageUrl, 각종 추가정보들...){
//         return fastApiClient.추천결과받기();
//    }


}
