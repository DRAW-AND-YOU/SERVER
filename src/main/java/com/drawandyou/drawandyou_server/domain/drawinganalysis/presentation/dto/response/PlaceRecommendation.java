package com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response;

public record PlaceRecommendation(
        String title,
        String address,
        String url,
        Double rating,
        String image
) {
}
