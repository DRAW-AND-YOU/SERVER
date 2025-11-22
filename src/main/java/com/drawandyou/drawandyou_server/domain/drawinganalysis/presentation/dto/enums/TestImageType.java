package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TestImageType {

    ONE(1 , "HTP-house"),
    TWO(2, "HTP-tree"),
    THREE(3, "HTP-person"),
    FOUR(4, "PITR");

    private final Integer number;
    private final String testType;
}
