package com.drawandyou.drawandyou_server.domain.diary.application.service;

import com.drawandyou.drawandyou_server.domain.diary.domain.entity.Diary;
import com.drawandyou.drawandyou_server.domain.diary.domain.repository.DiaryRepository;
import com.drawandyou.drawandyou_server.domain.diary.exception.DiaryExistsException;
import com.drawandyou.drawandyou_server.domain.diary.presentation.dto.request.DiaryCreateRequest;
import com.drawandyou.drawandyou_server.global.client.fastapi.dto.response.DiaryImageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiarySaveService {

    private final DiaryRepository diaryRepository;

    @Transactional
    public Diary save(Long userId, DiaryCreateRequest request, DiaryImageResponse response) {

        // writtenAt을 기준으로, 동일한 날짜에 작성한 일기가 있다면 예외 던지기
        LocalDateTime startOfDay = request.writtenAt().toLocalDate().atStartOfDay();
        LocalDateTime startOfNextDay = request.writtenAt().toLocalDate().plusDays(1).atStartOfDay();

        boolean diaryExists = diaryRepository.existsByUserIdAndWrittenAtBetween(userId, startOfDay, startOfNextDay);
        if (diaryExists){
            throw new DiaryExistsException();
        }

        Diary diary = Diary.create(userId, request.title(), request.content(),
                response.imageUrl(), request.writtenAt(), request.keyword());

        return diaryRepository.save(diary);
    }

    @Transactional
    public Diary save(Long userId, DiaryCreateRequest request) {

        LocalDateTime startOfDay = request.writtenAt().toLocalDate().atStartOfDay();
        LocalDateTime startOfNextDay = request.writtenAt().toLocalDate().plusDays(1).atStartOfDay();

        boolean diaryExists = diaryRepository.existsByUserIdAndWrittenAtBetween(userId, startOfDay, startOfNextDay);
        if (diaryExists){
            throw new DiaryExistsException();
        }

        Diary diary = Diary.create(userId, request.title(), request.content(),
                null, request.writtenAt(), request.keyword());
        return diaryRepository.save(diary);
    }
}
