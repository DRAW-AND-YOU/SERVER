package com.drawandyou.drawandyou_server.domain.dailycourse.application.service;

import com.drawandyou.drawandyou_server.domain.dailycourse.domain.repository.DailyCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailyCourseFindService {

    private final DailyCourseRepository dailyCourseRepository;
}
