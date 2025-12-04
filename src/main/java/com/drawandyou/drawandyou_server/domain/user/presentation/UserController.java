package com.drawandyou.drawandyou_server.domain.user.presentation;

import com.drawandyou.drawandyou_server.domain.user.presentation.dto.request.*;
import com.drawandyou.drawandyou_server.domain.user.presentation.dto.response.*;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import com.drawandyou.drawandyou_server.domain.user.application.service.UserService;
import com.drawandyou.drawandyou_server.domain.user.presentation.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
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
    public ApiResponse<RegisterResponse> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {

        RegisterResponse response = userService.registerUser(registerRequest);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.USER_SIGNUP_SUCCESS.getMessage(), response);
    }

    /**
     * 일반 로그인 - 로그인
     */
    @Operation(summary = "일반 로그인")
    @PostMapping("/signin")
    public ApiResponse<LoginResponse> authenticate(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        LoginResponse loginResponse = userService.signIn(loginRequest.email(), loginRequest.password());

        // ResponseCookie를 사용하여 액세스 토큰을 HttpOnly 쿠키에 설정
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", loginResponse.accessToken())
                .httpOnly(true)
                .secure(true)
                .domain(".drawandyou.com")
                .path("/")
                .maxAge(Duration.ofHours(1))
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());

        return ApiResponse.success(HttpStatus.OK, ResponseMessage.USER_SIGNIN_SUCCESS.getMessage(), loginResponse);
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
        log.info("로그아웃 요청 시작");

        try {
            // ResponseCookie로 쿠키 삭제 (maxAge를 0으로 설정)
            ResponseCookie deleteCookie = ResponseCookie.from("accessToken", "")
                    .httpOnly(true)                    // JavaScript 접근 차단 (XSS 방어)
                    .secure(true)                      // HTTPS에서만 전송
                    .domain(".drawandyou.com")         // 서브도메인 간 쿠키 공유
                    .path("/")                         // 모든 경로에서 쿠키 전송
                    .maxAge(0)                         // 즉시 만료 (쿠키 삭제)
                    .sameSite("None")                  // 크로스 사이트 전송 허용
                    .build();

            response.addHeader("Set-Cookie", deleteCookie.toString());
            log.info("로그아웃 성공");

            return ApiResponse.success(HttpStatus.OK, ResponseMessage.USER_LOGOUT_SUCCESS.getMessage());
        } catch (Exception e) {
            log.error("로그아웃 처리 중 에러 발생", e);
            throw e;
        }
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

    @Operation(summary = "소셜 로그인 유저 추가 회원가입")
    @PostMapping("/signup/extra")
    public ApiResponse<RegisterResponse> processExtraSignUpForSocialLoginUser(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid ExtraRegisterRequest extraRegisterRequest){
        RegisterResponse response = userService.processExtraSignUpForSocialLoginUser(userId, extraRegisterRequest);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.SOCIAL_LOGIN_USER_EXTRA_SIGN_UP_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "마이페이지 - 프로필 이미지 변경")
    @PatchMapping("/mypage/profileImage")
    public ApiResponse<UserMyPageResponse> changeProfileImageInMyPage(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid ProfileImageChangeRequest profileImageChangeRequest
            ){

        UserMyPageResponse response = userService.changeProfileImageUrlForUser(userId, profileImageChangeRequest);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.USER_PROFILE_IMAGE_CHANGE_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "마이페이지 - 비밀번호 변경")
    @PatchMapping("/mypage/password")
    public ApiResponse<UserMyPageResponse> changePasswordInMyPage(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid PasswordChangeRequest passwordChangeRequest
    ){
        UserMyPageResponse response = userService.changePasswordForUser(userId, passwordChangeRequest);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.USER_PASSWORD_CHANGE_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "마이페이지 - 관심사 변경")
    @PatchMapping("/mypage/hobbies")
    public ApiResponse<UserMyPageResponse> changeHobbiesInMyPage(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid HobbiesChangeRequest hobbiesChangeRequest
    ){
        UserMyPageResponse response = userService.changeHobbiesForUser(userId, hobbiesChangeRequest);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.USER_HOBBIES_CHANGE_SUCCESS.getMessage(), response);
    }



}
