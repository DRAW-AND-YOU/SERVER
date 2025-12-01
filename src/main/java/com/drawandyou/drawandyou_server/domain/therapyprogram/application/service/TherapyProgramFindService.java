package com.drawandyou.drawandyou_server.domain.therapyprogram.application.service;

import com.drawandyou.drawandyou_server.domain.dailycourse.domain.entity.DailyCourse;
import com.drawandyou.drawandyou_server.domain.dailycourse.domain.repository.DailyCourseRepository;
import com.drawandyou.drawandyou_server.domain.therapyprogram.domain.entity.TherapyProgram;
import com.drawandyou.drawandyou_server.domain.therapyprogram.domain.repository.TherapyProgramRepository;
import com.drawandyou.drawandyou_server.domain.therapyprogram.exception.TherapyProgramNotFoundException;
import com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.dto.response.OngoingProgramResponse;
import com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.dto.response.ParticipatedProgramIdResponse;
import com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.dto.response.TherapyProgramInfoResponse;
import com.drawandyou.drawandyou_server.domain.user.application.service.UserFindService;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TherapyProgramFindService {

    private final TherapyProgramRepository therapyProgramRepository;
    private final DailyCourseRepository dailyCourseRepository;
    private final UserFindService userFindService;

    private static final int DEFAULT_TOTAL_DAYS = 5;

    public OngoingProgramResponse getOngoingProgramInfo(Long userId) {
        User user = userFindService.findUser(userId);

        boolean isProgramFinished = false;
        Optional<TherapyProgram> therapyProgramOpt = therapyProgramRepository.findByUserAndIsFinished(user, isProgramFinished);

        // 데일리 코스 5일차까지 끝내고, 현황 조회하는 경우 Opt 는 empty 로 조회된다.
        if (therapyProgramOpt.isEmpty()){
            TherapyProgram finishedProgram = therapyProgramRepository.findLatestByUserAndIsFinishedOrderByEndDateDesc(user, true);
            return new OngoingProgramResponse(finishedProgram.getId(), 5,DEFAULT_TOTAL_DAYS,null, true);
        }

        TherapyProgram therapyProgram = therapyProgramOpt.get();
        boolean isCourseFinished = true;
        int completeCourseCount = dailyCourseRepository.countByTherapyProgramAndIsCompleted(therapyProgram, isCourseFinished);

        // 현재 진행해야 할 DailyCourse ID 조회
        Long currentDailyCourseId = dailyCourseRepository
                .findFirstByTherapyProgramAndIsCompletedOrderByCurrentDayAsc(therapyProgram, false)
                .map(DailyCourse::getId)
                .orElse(null);  // 모든 코스가 완료된 경우 null

        return new OngoingProgramResponse(therapyProgram.getId(), completeCourseCount, therapyProgram.getTotalDays(), currentDailyCourseId, true);
    }

    public ParticipatedProgramIdResponse getTherapyProgramInfos(Long userId) {
        User user = userFindService.findUser(userId);
        List<TherapyProgramInfoResponse> infos = therapyProgramRepository.findTherapyProgramsByUserAndIsFinished(user, true);

        return new ParticipatedProgramIdResponse(infos);
    }
}
