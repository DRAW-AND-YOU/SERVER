package com.drawandyou.drawandyou_server.domain.diary.application.service;

import com.drawandyou.drawandyou_server.domain.diary.domain.entity.Diary;
import com.drawandyou.drawandyou_server.domain.diary.domain.repository.DiaryRepository;
import com.drawandyou.drawandyou_server.domain.diary.presentation.dto.request.DiaryCreateRequest;
import com.drawandyou.drawandyou_server.global.client.fastapi.dto.response.DiaryImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiarySaveService {

    private final DiaryRepository diaryRepository;

    @Transactional
    public Diary save(Long userId, DiaryCreateRequest request, DiaryImageResponse response) {

        Diary diary = Diary.create(userId, request.title(), request.content(),
                response.imageUrl(), request.writtenAt(), request.keyword());

        return diaryRepository.save(diary);
    }

    @Transactional
    public Diary save(Long userId, DiaryCreateRequest request) {
        Diary diary = Diary.create(userId, request.title(), request.content(),
                null, request.writtenAt(), request.keyword());
        return diaryRepository.save(diary);
    }
}
