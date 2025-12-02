package com.drawandyou.drawandyou_server.domain.diary.application.service;

import com.drawandyou.drawandyou_server.domain.diary.domain.entity.Diary;
import com.drawandyou.drawandyou_server.domain.diary.domain.repository.DiaryRepository;
import com.drawandyou.drawandyou_server.domain.diary.exception.DiaryExistsException;
import com.drawandyou.drawandyou_server.domain.diary.exception.NotDiaryOwnerException;
import com.drawandyou.drawandyou_server.domain.diary.presentation.dto.request.DiaryCreateRequest;
import com.drawandyou.drawandyou_server.domain.diary.presentation.dto.response.DiaryCalendarResponse;
import com.drawandyou.drawandyou_server.domain.diary.presentation.dto.response.DiaryResponse;
import com.drawandyou.drawandyou_server.global.client.fastapi.FastApiClient;
import com.drawandyou.drawandyou_server.global.client.fastapi.dto.request.DiaryImageRequest;
import com.drawandyou.drawandyou_server.global.client.fastapi.dto.response.DiaryImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final FastApiClient fastApiClient;
    private final DiarySaveService diarySaveService;
    private final DiaryFindService diaryFindService;

    private final DiaryRepository diaryRepository;

    // 외부 api 호출은 트랜잭션 밖에서 수행
    public DiaryResponse writeDiary(Long userId, DiaryCreateRequest request) {

        // 1. fast api client 에 request body 로 content 를  넘겨준다.
        // 2. fast api 에서 이미지 생성 및 S3 에 업로드 . 생성한 imageUrl 을 반환

        // writtenAt을 기준으로, 동일한 날짜에 작성한 일기가 있다면 예외 던지기
        LocalDateTime startOfDay = request.writtenAt().toLocalDate().atStartOfDay();
        LocalDateTime startOfNextDay = request.writtenAt().toLocalDate().plusDays(1).atStartOfDay();

        boolean diaryExists = diaryRepository.existsByUserIdAndWrittenAtBetween(userId, startOfDay, startOfNextDay);
        if (diaryExists){
            throw new DiaryExistsException();
        }

        DiaryImageRequest diaryImageRequest = new DiaryImageRequest(request.content());
        DiaryImageResponse imageResponse = fastApiClient.generateDiaryImageSync(diaryImageRequest);
        // diary 저장은 트랜잭션 내부에서 수행
        Diary savedDiary = diarySaveService.save(userId, request, imageResponse);
        return DiaryResponse.from(savedDiary);
    }

    @Transactional
    public void delete(Long userId, Long diaryId) {

        Diary diary = findDiaryAndVerifyOwner(userId, diaryId);
        diaryRepository.delete(diary);
    }

    private Diary findDiaryAndVerifyOwner(Long userId, Long diaryId) {
        Diary diary = diaryFindService.findById(diaryId);

        // user 의 diary 소유권 검증
        if (!diary.getAuthorId().equals(userId)){
            throw new NotDiaryOwnerException();
        }
        return diary;
    }

    public DiaryResponse getDiary(Long userId, Long diaryId) {

        Diary diary = findDiaryAndVerifyOwner(userId, diaryId);
        return DiaryResponse.from(diary);
    }

    public DiaryCalendarResponse getDiariesForCalendar(Long userId, LocalDate startDate, LocalDate endDate) {
       return  diaryRepository.findDiariesForCalendar(userId, startDate, endDate);
    }

}
