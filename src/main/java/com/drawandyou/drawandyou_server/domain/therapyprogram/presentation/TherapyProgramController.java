package com.drawandyou.drawandyou_server.domain.therapyprogram.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "THERAPY PROGRAM", description = "치유 프로그램 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/programs")
public class TherapyProgramController {
}
