package com.drawandyou.drawandyou_server.global.client.fastapi;

import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.request.ContentRecommendRequest;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.FastApiRecommendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FastApiClient {

    private final WebClient webClient;

    /**
     * FastAPI의 /recommendation 엔드포인트를 호출하여 그림 분석 및 콘텐츠 추천을 받습니다.
     *
     * @param request 콘텐츠 추천 요청 정보 (이미지 URL, 이미지 타입, 후질문 응답, 위치 정보)
     * @return FastAPI 응답 (그림 분석 결과 + 콘텐츠 추천)
     */
    public Mono<FastApiRecommendResponse> getContentRecommendations(ContentRecommendRequest request) {
        return webClient.post()
                .uri("/recommendation")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(FastApiRecommendResponse.class)
                .doOnSuccess(response -> log.info("Content Recommendation 응답 성공: {}", response))
                .doOnError(error -> log.error("Content Recommendation 호출 실패", error));
    }

    /**
     * FastAPI의 /recommend 엔드포인트를 동기적으로 호출합니다.
     *
     * @param request 콘텐츠 추천 요청 정보
     * @return FastAPI 응답 (그림 분석 결과 + 콘텐츠 추천)
     */
    public FastApiRecommendResponse getContentRecommendationsSync(ContentRecommendRequest request) {
        try {
            return getContentRecommendations(request).block();
        } catch (Exception e) {
            log.error("Content Recommendation 동기 호출 실패", e);
            throw new RuntimeException("Content Recommendation 호출 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}
