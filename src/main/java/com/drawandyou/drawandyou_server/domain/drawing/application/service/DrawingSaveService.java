package com.drawandyou.drawandyou_server.domain.drawing.application.service;

import com.drawandyou.drawandyou_server.domain.drawing.domain.entity.Drawing;
import com.drawandyou.drawandyou_server.domain.drawing.domain.repository.DrawingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DrawingSaveService {

    private final DrawingRepository drawingRepository;

    public Drawing save(Drawing drawing){
        return drawingRepository.save(drawing);
    }

}
