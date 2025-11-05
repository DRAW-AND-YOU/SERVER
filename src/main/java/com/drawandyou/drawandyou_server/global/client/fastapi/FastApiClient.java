package com.drawandyou.drawandyou_server.global.client.fastapi;

import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.DetailedScores;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.request.ContentRecommendRequest;
import com.drawandyou.drawandyou_server.domain.drawinganalysis.presentation.dto.response.ContentRecommendationResponse;
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
     * FastAPI의 /test/spotifymcp/tools 엔드포인트를 호출합니다.
     *
     * @return API 응답 결과
     */
    public Mono<String> getSpotifyMcpTools() {
        return webClient.get()
                .uri("/test/spotifymcp/tools")
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("FastAPI 응답 성공: {}", response))
                .doOnError(error -> log.error("FastAPI 호출 실패", error));
    }

    public Mono<String> searchPlacesByKeyword(String query){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/test/kakao/search/keyword")
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                    .doOnSuccess(response -> log.info("Kakao Place Search 응답 성공 (keyword: {}): {}", query, response))
                .doOnError(error -> log.error("Kakao Place Search 호출 실패 (keyword: {})", query, error));
    }

    public String searchPlacesByKeywordSync(String keyword){
        return searchPlacesByKeyword(keyword).block();
    }



    /**
     * FastAPI의 /test/spotifymcp/tools 엔드포인트를 동기적으로 호출합니다.
     *
     * @return API 응답 결과
     */
    public String getSpotifyMcpToolsSync() {
        try {
            return getSpotifyMcpTools().block();
        } catch (Exception e) {
            log.error("FastAPI 동기 호출 실패", e);
            throw new RuntimeException("FastAPI 호출 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * FastAPI의 /test/spotifymcp/recommendations 엔드포인트를 호출합니다.
     *
     * @param mood 음악 분위기 (happy, sad, calm, energetic, anxious)
     * @param limit 추천 곡 개수
     * @return API 응답 결과
     */
    public Mono<String> getSpotifyRecommendations(String mood, int limit) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/test/spotifymcp/recommendations")
                        .queryParam("mood", mood)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("Spotify Recommendations 응답 성공 (mood: {}): {}", mood, response))
                .doOnError(error -> log.error("Spotify Recommendations 호출 실패 (mood: {})", mood, error));
    }

    /**
     * FastAPI의 /test/spotifymcp/recommendations 엔드포인트를 동기적으로 호출합니다.
     *
     * @param mood 음악 분위기
     * @param limit 추천 곡 개수
     * @return API 응답 결과
     */
    public String getSpotifyRecommendationsSync(String mood, int limit) {
        try {
            return getSpotifyRecommendations(mood, limit).block();
        } catch (Exception e) {
            log.error("Spotify Recommendations 동기 호출 실패 (mood: {})", mood, e);
            throw new RuntimeException("Spotify Recommendations 호출 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * FastAPI의 /test/youtubemcp/tools 엔드포인트를 호출합니다.
     *
     * @return API 응답 결과
     */
    public Mono<String> getYoutubeMcpTools() {
        return webClient.get()
                .uri("/test/youtubemcp/tools")
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("YouTube MCP Tools 응답 성공: {}", response))
                .doOnError(error -> log.error("YouTube MCP Tools 호출 실패", error));
    }

    /**
     * FastAPI의 /test/youtubemcp/tools 엔드포인트를 동기적으로 호출합니다.
     *
     * @return API 응답 결과
     */
    public String getYoutubeMcpToolsSync() {
        try {
            return getYoutubeMcpTools().block();
        } catch (Exception e) {
            log.error("YouTube MCP Tools 동기 호출 실패", e);
            throw new RuntimeException("YouTube MCP Tools 호출 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * FastAPI의 /test/youtubemcp/search 엔드포인트를 호출합니다.
     *
     * @param query 검색 키워드
     * @param maxResults 최대 결과 개수
     * @return API 응답 결과
     */
    public Mono<String> searchYoutubeVideos(String query, int maxResults) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/test/youtubemcp/search")
                        .queryParam("query", query)
                        .queryParam("maxResults", maxResults)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("YouTube Video Search 응답 성공 (query: {}): {}", query, response))
                .doOnError(error -> log.error("YouTube Video Search 호출 실패 (query: {})", query, error));
    }

    /**
     * FastAPI의 /test/youtubemcp/search 엔드포인트를 동기적으로 호출합니다.
     *
     * @param query 검색 키워드
     * @param maxResults 최대 결과 개수
     * @return API 응답 결과
     */
    public String searchYoutubeVideosSync(String query, int maxResults) {
        try {
            return searchYoutubeVideos(query, maxResults).block();
        } catch (Exception e) {
            log.error("YouTube Video Search 동기 호출 실패 (query: {})", query, e);
            throw new RuntimeException("YouTube Video Search 호출 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * FastAPI의 /test/recommendation/ 엔드포인트를 호출합니다.
     *
     * @param score 점수
     * @return API 응답 결과
     */
    public Mono<String> testRecommendation(int score) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/test/recommendation/")
                        .queryParam("score", score)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("Test Recommendation 응답 성공 (score: {}): {}", score, response))
                .doOnError(error -> log.error("Test Recommendation 호출 실패 (score: {})", score, error));
    }

    /**
     * FastAPI의 /test/recommendation/ 엔드포인트를 동기적으로 호출합니다.
     *
     * @param score 점수
     * @return API 응답 결과
     */
    public String testRecommendationSync(int score) {
        try {
            return testRecommendation(score).block();
        } catch (Exception e) {
            log.error("Test Recommendation 동기 호출 실패 (score: {})", score, e);
            throw new RuntimeException("Test Recommendation 호출 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * FastAPI의 /recommendation/ 엔드포인트를 호출하여 콘텐츠 추천을 받습니다.
     *
     * @param request 콘텐츠 추천 요청 정보 (점수, 세부 점수, 위치 정보)
     * @return 콘텐츠 추천 응답 (음악, 비디오, 장소 추천)
     */
    public Mono<ContentRecommendationResponse> getContentRecommendations(ContentRecommendRequest request) {
        return webClient.post()
                .uri("/recommendation")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ContentRecommendationResponse.class)
                .doOnSuccess(response -> log.info("Content Recommendation 응답 성공 (score: {}): {}", request.score(), response))
                .doOnError(error -> log.error("Content Recommendation 호출 실패 (score: {})", request.score(), error));
    }

    /**
     * FastAPI의 /recommendation/ 엔드포인트를 동기적으로 호출합니다.
     *
     * @param request 콘텐츠 추천 요청 정보
     * @return 콘텐츠 추천 응답
     */
    public ContentRecommendationResponse getContentRecommendationsSync(ContentRecommendRequest request) {
        try {
            return getContentRecommendations(request).block();
        } catch (Exception e) {
            log.error("Content Recommendation 동기 호출 실패 (score: {})", request.score(), e);
            throw new RuntimeException("Content Recommendation 호출 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 테스트용 임시 데이터로 콘텐츠 추천을 요청합니다.
     *
     * @return 콘텐츠 추천 응답
     */
    public ContentRecommendationResponse getContentRecommendationsWithTestData() {
        // 임시 테스트 데이터 생성
        DetailedScores detailedScores = new DetailedScores(40, 40, 20);
        ContentRecommendRequest request = new ContentRecommendRequest(
                100,
                detailedScores,
                37.5050881,  // 서울 위도
                126.9571012  // 서울 경도
        );

        return getContentRecommendationsSync(request);
    }
}
