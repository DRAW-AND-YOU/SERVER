package com.drawandyou.drawandyou_server.domain.user.presentation;

import com.drawandyou.drawandyou_server.domain.user.presentation.dto.response.*;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import com.drawandyou.drawandyou_server.domain.user.application.service.UserService;
import com.drawandyou.drawandyou_server.domain.user.presentation.dto.request.LoginRequest;
import com.drawandyou.drawandyou_server.domain.user.presentation.dto.request.RegisterRequest;
import com.drawandyou.drawandyou_server.domain.user.presentation.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "USER", description = "사용자 관련 API")
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

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setDomain(".drawandyou.com");

        response.addCookie(cookie);

        return ApiResponse.success(HttpStatus.OK, ResponseMessage.USER_LOGOUT_SUCCESS.getMessage());
    }

    @Operation(summary = "회원가입시 중복 ID 검증")
    @GetMapping("/check-username")
    public ApiResponse<CheckUserNameResponse> checkUserNameAvailable(@RequestParam String username){
        CheckUserNameResponse isAvailable = userService.checkUsernameAvailable(username);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.USER_NAME_AVAILABLE_CHECK_SUCCESS.getMessage(), isAvailable);
    }

    @Operation(summary = "마이페이지 조회")
    @GetMapping("/mypage")
    public ApiResponse<UserMyPageResponse> getUserMyPage(@AuthenticationPrincipal Long userId){
        UserMyPageResponse response  = userService.getUserMyPage(userId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.USER_MY_PAGE_GET_SUCCESS.getMessage(), response);
    }


}
