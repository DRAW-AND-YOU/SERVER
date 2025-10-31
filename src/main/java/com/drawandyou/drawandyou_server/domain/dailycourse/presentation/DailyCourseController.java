package com.drawandyou.drawandyou_server.domain.dailycourse.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "DAILY COURSE", description = "치유 프로그램 코스(1일차~5일차) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class DailyCourseController {
}
