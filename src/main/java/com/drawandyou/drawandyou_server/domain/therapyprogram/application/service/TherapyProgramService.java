package com.drawandyou.drawandyou_server.domain.therapyprogram.application.service;

import com.drawandyou.drawandyou_server.domain.dailycourse.domain.entity.DailyCourse;
import com.drawandyou.drawandyou_server.domain.dailycourse.domain.entity.enums.CourseType;
import com.drawandyou.drawandyou_server.domain.dailycourse.domain.repository.DailyCourseRepository;
import com.drawandyou.drawandyou_server.domain.dailycourse.presentation.dto.DailyCourseScoreResponse;
import com.drawandyou.drawandyou_server.domain.therapyprogram.domain.entity.TherapyProgram;
import com.drawandyou.drawandyou_server.domain.therapyprogram.domain.repository.TherapyProgramRepository;
import com.drawandyou.drawandyou_server.domain.therapyprogram.exception.AlreadyParticipateInProgramException;
import com.drawandyou.drawandyou_server.domain.therapyprogram.exception.NotAllCoursesCompletedException;
import com.drawandyou.drawandyou_server.domain.therapyprogram.exception.TherapyProgramAccessDeniedException;
import com.drawandyou.drawandyou_server.domain.therapyprogram.exception.TherapyProgramNotFoundException;
import com.drawandyou.drawandyou_server.domain.therapyprogram.presentation.dto.response.TherapyProgramScoreResponse;
import com.drawandyou.drawandyou_server.domain.user.application.service.UserFindService;
import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TherapyProgramService {

    private static final int TOTAL_DAILY_COURSE_COUNT = 5;

    private final TherapyProgramSaveService therapyProgramSaveService;
    private final UserFindService userFindService;

    private final TherapyProgramRepository therapyProgramRepository;
    private final DailyCourseRepository dailyCourseRepository;

    /**
     * 치유 프로그램 첫 등록하기
     * @param userId
     */
    @Transactional
    public void enrollTherapyProgram(Long userId) {

        User user = userFindService.findUser(userId);

        // 이미 유저가 참여중(아직 코스 완주를 못한)인 프로그램이 있다면, 등록하지 못하므로 예외처리한다.
        boolean isFinished = false;
        boolean isParticipating =  therapyProgramRepository.existsByUserAndIsFinished(user, isFinished);
        if (isParticipating) throw new AlreadyParticipateInProgramException();

        // 프로그램과 유저 간의 연관관계 할당
        TherapyProgram therapyProgram = TherapyProgram.createAndEnrollUser(user);
        TherapyProgram saveTherapyProgram = therapyProgramSaveService.saveTherapyProgram(therapyProgram);

        CourseType[] courseTypes = CourseType.values();

        List<DailyCourse> dailyCourses = new ArrayList<>();
        // 치유 프로그램이 만들어지고 나서, DailyCourse(5개) 와 연관관계를 맺도록 한다.
        for (int day = 1; day <= TOTAL_DAILY_COURSE_COUNT; day++){
            CourseType courseType = courseTypes[day -1];

            DailyCourse dailyCourse = DailyCourse.builder()
                    .therapyProgram(saveTherapyProgram)
                    .currentDay(day)
                    .courseType(courseType)
                    .isCompleted(false)
                    .build();

            dailyCourses.add(dailyCourse);
        }

        dailyCourseRepository.saveAll(dailyCourses);
    }

    /**
     * 치유 프로그램 완주하기
     * @param userId
     */
    @Transactional
    public void completeProgram(Long userId) {

        User user = userFindService.findUser(userId);

        boolean isFinished = false;
        TherapyProgram therapyProgram = therapyProgramRepository.findByUserAndIsFinished(user, isFinished)
                .orElseThrow(TherapyProgramNotFoundException::new);

        // 치유 프로그램의 isFinished 를 true 로 변경
        therapyProgram.changeStatusToFinish();
        // 치유 프로그램의 종료 날짜를 현재 시점으로 설정
        therapyProgram.assignEndDate();
    }

    public TherapyProgramScoreResponse getTherapyProgramScores(Long userId, Long therapyProgramId) {
        TherapyProgram therapyProgram = therapyProgramRepository.findById(therapyProgramId)
                .orElseThrow(TherapyProgramAccessDeniedException::new);

        validateUserOwnerShip(userId, therapyProgram);
        List<DailyCourseScoreResponse> scoreResponses = dailyCourseRepository.findScoresByTherapyProgramId(therapyProgramId);
        return new TherapyProgramScoreResponse(scoreResponses);
    }

    private static void validateUserOwnerShip(Long userId, TherapyProgram therapyProgram) {
        if (!therapyProgram.getUser().getId().equals(userId)){
            throw new TherapyProgramAccessDeniedException();
        }
    }

    @Transactional
    public void completeTherapyProgram(Long userId, Long therapyProgramId) {
        TherapyProgram therapyProgram = therapyProgramRepository.findById(therapyProgramId)
                .orElseThrow(TherapyProgramNotFoundException::new);

        // 소유권 검증
        validateUserOwnerShip(userId, therapyProgram);
        // 모든 데일리 코스가 완료되었는지 확인
        int completedCount = dailyCourseRepository.countByTherapyProgramAndIsCompleted(therapyProgram, true);
        if (completedCount < TOTAL_DAILY_COURSE_COUNT) {
            throw new NotAllCoursesCompletedException();
        }

        therapyProgram.changeStatusToFinish();
    }
}
