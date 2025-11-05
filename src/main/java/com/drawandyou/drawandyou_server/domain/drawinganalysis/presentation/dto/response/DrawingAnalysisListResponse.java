package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response;

import com.drawandyou.drawandyou_server.global.common.response.PageInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public record DrawingAnalysisListResponse(
        List<DrawingAnalysisSimpleResponse> drawingAnalysisList,
        PageInfo pageInfo
) {
    public static DrawingAnalysisListResponse of(Page<DrawingAnalysisSimpleResponse> page) {
        return new DrawingAnalysisListResponse(
                page.getContent(),
                PageInfo.from(page)
        );
    }
}