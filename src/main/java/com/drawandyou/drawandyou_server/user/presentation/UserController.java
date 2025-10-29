package com.drawandyou.drawandyou_server.user.presentation;

import com.drawandyou.drawandyou_server.global.auth.presentation.dto.UserAuthDto;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import com.drawandyou.drawandyou_server.user.application.service.UserService;
import com.drawandyou.drawandyou_server.user.presentation.dto.request.LoginRequest;
import com.drawandyou.drawandyou_server.user.presentation.dto.request.RegisterRequest;
import com.drawandyou.drawandyou_server.user.presentation.dto.response.CurrentLoginUserResponse;
import com.drawandyou.drawandyou_server.user.presentation.dto.response.LoginResponse;
import com.drawandyou.drawandyou_server.user.presentation.dto.response.RegisterResponse;
import com.drawandyou.drawandyou_server.user.presentation.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    /**
     * 일반 로그인 - 회원가입
     */
    @Operation(summary = "일반 회원가입")
    @PostMapping("/signup")
    public ApiResponse<RegisterResponse> registerUser(@RequestBody RegisterRequest registerRequest) {

        RegisterResponse response = userService.registerUser(registerRequest);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.USER_SIGNUP_SUCCESS.getMessage(), response);
    }

    /**
     * 일반 로그인 - 로그인
     */
    @Operation(summary = "일반 로그인")
    @PostMapping("/signin")
    public ApiResponse<LoginResponse> authenticate(@RequestBody LoginRequest loginRequest) {

        LoginResponse response = userService.signIn(loginRequest.username(), loginRequest.password());
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.USER_SIGNIN_SUCCESS.getMessage(), response);
    }

    /**
     * 현재 인증된 사용자 정보 조회
     * HttpOnly 쿠키 또는 Authorization 헤더에서 JWT 토큰을 통해 인증된 사용자 정보 반환
     * @param userId JWT 토큰에서 추출된 사용자 ID (JwtAuthenticationFilter에서 SecurityContext에 설정됨)
     * @return 사용자 정보 (비밀번호 제외)
     */
    @Operation(summary = "현재 로그인 사용자 정보 조회")
    @GetMapping("/me")
    public ApiResponse<CurrentLoginUserResponse> getCurrentUser(@AuthenticationPrincipal Long userId) {

        // userId는 JWT 토큰에서 추출되어 SecurityContext에 설정된 값
        CurrentLoginUserResponse response = userService.getUserById(userId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.USER_INFO_SUCCESS.getMessage(), response);
    }
}
