package com.drawandyou.drawandyou_server.domain.user.presentation.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {

    TEST_SUCCESS("유저 테스트에 성공하였습니다"),
    USER_SIGNUP_SUCCESS("회원가입에 성공했습니다"),
    USER_SIGNIN_SUCCESS("로그인에 성공했습니다"),
    USER_INFO_SUCCESS("사용자 정보 조회에 성공했습니다"),
    TOKEN_ISSUE_SUCCESS("JWT 토큰 발급에 성공했습니다"),
    USER_LOGOUT_SUCCESS("로그아웃에 성공했습니다."),
    USER_NAME_AVAILABLE_CHECK_SUCCESS("유저 ID 중복 확인에 성공했습니다"),
    USER_MY_PAGE_GET_SUCCESS("마이페이지 조회에 성공했습니다"),
    SOCIAL_LOGIN_USER_EXTRA_SIGN_UP_SUCCESS("소셜 로그인 유저 추가 회원가입에 성공했습니다"),
    USER_PROFILE_IMAGE_CHANGE_SUCCESS("유저 프로필 이미지 변경에 성공했습니다."),
    USER_PASSWORD_CHANGE_SUCCESS("유저 비밀번호 변경에 성공했습니다."),
    USER_HOBBIES_CHANGE_SUCCESS("유저 관심사 목록 변경에 성공했습니다."),
    USER_DASHBOARD_STATS_GET_SUCCESS("유저 대시보드 통계 정보 조회에 성공했습니다.");

    private final String message;
}
