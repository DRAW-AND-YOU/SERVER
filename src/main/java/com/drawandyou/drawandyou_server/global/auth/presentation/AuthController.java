package com.drawandyou.drawandyou_server.global.auth.presentation;

import com.drawandyou.drawandyou_server.global.auth.presentation.dto.UserAuthDto;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import com.drawandyou.drawandyou_server.user.application.service.UserService;
import com.drawandyou.drawandyou_server.user.presentation.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    /**
     * 사용자 ID로 JWT 토큰 발급
     * @param userId 토큰을 발급받을 사용자 ID
     * @return 사용자 정보 및 JWT 토큰
     */
    @Operation(summary = "사용자 ID로 JWT 토큰 발급", description = "테스트용 API - 사용자 ID를 입력하면 해당 사용자의 JWT 토큰을 발급합니다")
    @PostMapping("/issue-token/{userId}")
    public ApiResponse<UserAuthDto> issueToken(@PathVariable Long userId) {

        UserAuthDto response = userService.issueTokenByUserId(userId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.TOKEN_ISSUE_SUCCESS.getMessage(), response);
    }
}
