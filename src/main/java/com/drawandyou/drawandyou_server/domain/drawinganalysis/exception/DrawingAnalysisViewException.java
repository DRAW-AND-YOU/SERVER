package com.drawandyou.drawandyou_server.domain.drawinganalysis.exception;

import com.drawandyou.drawandyou_server.global.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DrawingAnalysisViewException extends CustomException {
    public DrawingAnalysisViewException() {
        super(HttpStatus.UNAUTHORIZED, ErrorMessage.CAN_NOT_VIEW_DRAWING_ANALYSIS.getMessage());
    }
}
