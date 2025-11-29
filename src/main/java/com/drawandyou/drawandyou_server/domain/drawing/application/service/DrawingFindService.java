package com.drawandyou.drawandyou_server.domain.drawing.application.service;

import com.drawandyou.drawandyou_server.domain.drawing.domain.entity.Drawing;
import com.drawandyou.drawandyou_server.domain.drawing.domain.repository.DrawingRepository;
import com.drawandyou.drawandyou_server.domain.drawing.exception.DrawingNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DrawingFindService {

    private final DrawingRepository drawingRepository;

    public Drawing findById(Long drawingId) {
        return drawingRepository.findById(drawingId)
                .orElseThrow(DrawingNotFoundException::new);
    }
}