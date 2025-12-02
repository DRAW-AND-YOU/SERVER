package com.drawandyou.drawandyou_server.domain.dailycourse.application.service;


import com.drawandyou.drawandyou_server.domain.dailycourse.domain.entity.DailyCourse;
import com.drawandyou.drawandyou_server.domain.dailycourse.domain.repository.DailyCourseRepository;
import com.drawandyou.drawandyou_server.domain.dailycourse.exception.DailyCourseAccessDeniedException;
import com.drawandyou.drawandyou_server.domain.dailycourse.exception.DailyCourseNotFoundException;
import com.drawandyou.drawandyou_server.domain.drawing.domain.entity.Drawing;
import com.drawandyou.drawandyou_server.domain.drawing.domain.repository.DrawingRepository;
import com.drawandyou.drawandyou_server.domain.drawing.exception.DrawingNotFoundException;
import com.drawandyou.drawandyou_server.domain.therapyprogram.application.service.TherapyProgramService;
import com.drawandyou.drawandyou_server.domain.user.application.service.UserFindService;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class DailyCourseService {

    private final UserFindService userFindService;
    private final TherapyProgramService therapyProgramService;

    private final DrawingRepository drawingRepository;
    private final DailyCourseRepository dailyCourseRepository;

    @Transactional
    public void completeDailyCourse(Long userId, Long dailyCourseId, Long drawingId) {

        User user = userFindService.findUser(userId);
        DailyCourse dailyCourse = dailyCourseRepository.findById(dailyCourseId)
                .orElseThrow(DailyCourseNotFoundException::new);

        // 특정 dailyCourse 를 진행중인 유저만 코스 완료를 할 수 있도록 한다. 그렇지 않다면 예외처리
        if(!dailyCourse.getTherapyProgram().getUser().equals(user)){
            throw new DailyCourseAccessDeniedException();
        }

        Drawing drawing = drawingRepository.findById(drawingId)
                .orElseThrow(DrawingNotFoundException::new);

        dailyCourse.assignDrawing(drawing);
        dailyCourse.changeStatusToCompleted();
    }
}
