package com.drawandyou.drawandyou_server.domain.therapyprogram.application.service;

import com.drawandyou.drawandyou_server.domain.dailycourse.domain.entity.DailyCourse;
import com.drawandyou.drawandyou_server.domain.dailycourse.domain.repository.DailyCourseRepository;
import com.drawandyou.drawandyou_server.domain.therapyprogram.domain.entity.TherapyProgram;
import com.drawandyou.drawandyou_server.domain.therapyprogram.domain.repository.TherapyProgramRepository;
import com.drawandyou.drawandyou_server.domain.therapyprogram.exception.TherapyProgramNotFoundException;
import com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.dto.response.OngoingProgramResponse;
import com.drawandyou.drawandyou_server.domain.user.application.service.UserFindService;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TherapyProgramFindService {

    private final TherapyProgramRepository therapyProgramRepository;
    private final DailyCourseRepository dailyCourseRepository;
    private final UserFindService userFindService;

    public boolean isUserAlreadyParticipating(Long userId){
        return therapyProgramRepository.existsByUserId(userId);
    }

    public OngoingProgramResponse getOngoingProgramInfo(Long userId) {
        User user = userFindService.findUser(userId);

        boolean isProgramFinished = false;
        TherapyProgram therapyProgram = therapyProgramRepository.findByUserAndIsFinished(user, isProgramFinished)
                .orElseThrow(TherapyProgramNotFoundException::new);

        boolean isCourseFinished = true;
        int completeCourseCount = dailyCourseRepository.countByTherapyProgramAndIsCompleted(therapyProgram, isCourseFinished);

        // 현재 진행해야 할 DailyCourse ID 조회
        Long currentDailyCourseId = dailyCourseRepository
                .findFirstByTherapyProgramAndIsCompletedOrderByCurrentDayAsc(therapyProgram, false)
                .map(DailyCourse::getId)
                .orElse(null);  // 모든 코스가 완료된 경우 null

        return new OngoingProgramResponse(completeCourseCount, therapyProgram.getTotalDays(), currentDailyCourseId);

    }
}
