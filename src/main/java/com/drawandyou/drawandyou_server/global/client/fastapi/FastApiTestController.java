package com.drawandyou.drawandyou_server.global.client.fastapi;

import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FastAPI 테스트", description = "FastAPI 연결 테스트 API")
@RestController
@RequestMapping("/api/fastapi")
@RequiredArgsConstructor
public class FastApiTestController {

    private final FastApiClient fastApiClient;

    @Operation(summary = "Spotify MCP Tools 조회", description = "FastAPI의 /test/spotifymcp/tools 엔드포인트를 호출합니다.")
    @GetMapping("/spotify-mcp-tools")
    public ApiResponse<String> testSpotifyMcpTools() {
        String result = fastApiClient.getSpotifyMcpToolsSync();
        return ApiResponse.success(HttpStatus.OK, "FastAPI 응답 성공", result);
    }

    @Operation(summary = "FastAPI 연결 테스트", description = "FastAPI 서버와의 연결 상태를 확인합니다.")
    @GetMapping("/health")
    public ApiResponse<String> testConnection() {
        try {
            fastApiClient.getSpotifyMcpToolsSync();
            return ApiResponse.success(HttpStatus.OK, "FastAPI 연결 성공", "연결 성공");
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "FastAPI 연결 실패: " + e.getMessage(), null);
        }
    }

    @Operation(summary = "키워드 기반 장소 검색", description = "FastAPI의 /test/kakao/search/keyword 엔드포인트를 호출합니다.")
    @GetMapping("/places")
    public ApiResponse<String> searchPlacesByKeyword(@RequestParam String keyword) {
        String result = fastApiClient.searchPlacesByKeywordSync(keyword);
        return ApiResponse.success(HttpStatus.OK, "키워드 기반 장소 검색에 성공하였습니다.", result);
    }




}
