package com.drawandyou.drawandyou_server.domain.therapyprogram.application.service;

import com.drawandyou.drawandyou_server.domain.therapyprogram.domain.entity.TherapyProgram;
import com.drawandyou.drawandyou_server.domain.therapyprogram.domain.repository.TherapyProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TherapyProgramSaveService {

    private final TherapyProgramRepository therapyProgramRepository;

    public TherapyProgram saveTherapyProgram(TherapyProgram therapyProgram){
        return therapyProgramRepository.save(therapyProgram);
    }
}
