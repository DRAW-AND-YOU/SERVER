package com.drawandyou.drawandyou_server.domain.diary.application.service;

import com.drawandyou.drawandyou_server.domain.diary.domain.entity.Diary;
import com.drawandyou.drawandyou_server.domain.diary.domain.repository.DiaryRepository;
import com.drawandyou.drawandyou_server.domain.diary.exception.DiaryNotFoundException;
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

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final FastApiClient fastApiClient;
    private final DiarySaveService diarySaveService;

    private final DiaryRepository diaryRepository;

    // 외부 api 호출은 트랜잭션 밖에서 수행
    public DiaryResponse writeDiary(Long userId, DiaryCreateRequest request) {

        // 1. fast api client 에 keyword, title, content 넘겨준다.
        // 2. fast api 에서 이미지 생성 및 S3 에 업로드 . 생성한 imageUrl 을 반환

        DiaryImageRequest diaryImageRequest = new DiaryImageRequest(request.keyword().getMessage(), request.title(), request.content());
        DiaryImageResponse imageResponse = fastApiClient.generateDiaryImageSync(diaryImageRequest);
        // diary 저장은 트랜잭션 내부에서 수행
        Diary savedDiary = diarySaveService.save(userId, request, imageResponse);
        return DiaryResponse.from(savedDiary);
    }

    @Transactional
    public void delete(Long userId, Long diaryId) {

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(DiaryNotFoundException::new);

        // user 의 diary 소유권 검증
        if (!diary.getAuthorId().equals(userId)){
            throw new NotDiaryOwnerException();
        }

        diaryRepository.delete(diary);
    }

    public DiaryResponse getDiary(Long userId, Long diaryId) {

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(DiaryNotFoundException::new);

        // user 의 diary 소유권 검증. 타인이 작성한 일기는 조회 불가능.
        if (!diary.getAuthorId().equals(userId)){
            throw new NotDiaryOwnerException();
        }
        return DiaryResponse.from(diary);
    }

    public DiaryCalendarResponse getDiariesForCalendar(Long userId, LocalDate startDate, LocalDate endDate) {
       return  diaryRepository.findDiariesForCalendar(userId, startDate, endDate);
    }

    public DiaryResponse writeDiaryTest(Long userId, DiaryCreateRequest request) {
        Diary savedDiary = diarySaveService.save(userId, request);
        return DiaryResponse.from(savedDiary);
    }
}
