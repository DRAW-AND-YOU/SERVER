package com.drawandyou.drawandyou_server.domain.user.presentation.dto.response;

public record DashBoardStatsResponse(
        Long drawingCount,
        Long articleCount,
        Long completedProgramCount
) {
    public static DashBoardStatsResponse of(Long drawingCount, Long articleCount, Long completedProgramCount){
        return new DashBoardStatsResponse(
                drawingCount,
                articleCount,
                completedProgramCount
        );
    }
}
