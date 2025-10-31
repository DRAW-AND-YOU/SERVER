package com.drawandyou.drawandyou_server.domain.drawing.presentation;

import com.drawandyou.drawandyou_server.domain.drawing.application.service.DrawingSaveService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "DRAWING", description = "그림(드로잉) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drawings")
public class DrawingController {

    private final DrawingSaveService drawingSaveService;
}
