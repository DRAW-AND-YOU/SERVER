package com.drawandyou.drawandyou_server.domain.user.presentation;

import com.drawandyou.drawandyou_server.domain.user.presentation.dto.request.*;
import com.drawandyou.drawandyou_server.domain.user.presentation.dto.response.*;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import com.drawandyou.drawandyou_server.domain.user.application.service.UserService;
import com.drawandyou.drawandyou_server.domain.user.presentation.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

        // 액세스 토큰을 HttpOnly 쿠키에 설정
        Cookie cookie = new Cookie("accessToken", loginResponse.accessToken());
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 1시간
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setDomain(".drawandyou.com");

        response.addCookie(cookie);

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
            log.info("쿠키 생성 시작");
            Cookie cookie = new Cookie("accessToken", "");
            log.info("쿠키 생성 완료");

            cookie.setPath("/");
            cookie.setMaxAge(0);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setDomain(".drawandyou.com");

            log.info("쿠키 설정 완료");
            response.addCookie(cookie);
            log.info("응답에 쿠키 추가 완료");

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
    public ApiResponse<Void> processExtraSignUpForSocialLoginUser(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid ExtraRegisterRequest extraRegisterRequest){
        userService.processExtraSignUpForSocialLoginUser(userId, extraRegisterRequest);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.SOCIAL_LOGIN_USER_EXTRA_SIGN_UP_SUCCESS.getMessage());
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
