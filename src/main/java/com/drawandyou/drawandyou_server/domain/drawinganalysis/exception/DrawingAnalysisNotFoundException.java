package com.drawandyou.drawandyou_server.domain.drawinganalysis.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DrawingAnalysisNotFoundException extends CustomException {
    public DrawingAnalysisNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorMessage.DRAWING_ANALYSIS_NOT_FOUND.getMessage());
    }
}
