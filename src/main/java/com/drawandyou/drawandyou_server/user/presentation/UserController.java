package com.drawandyou.drawandyou_server.user.presentation;

import com.drawandyou.drawandyou_server.global.auth.presentation.dto.UserAuthDto;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import com.drawandyou.drawandyou_server.user.application.service.UserService;
import com.drawandyou.drawandyou_server.user.presentation.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
     * @param userDTO
     * @return
     */
    @Operation(summary = "일반 회원가입")
    @PostMapping("/signup")
    public ApiResponse<UserAuthDto> registerUser(@RequestBody UserAuthDto userDTO) {

        UserAuthDto response = userService.registerUser(userDTO);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.USER_SIGNUP_SUCCESS.getMessage(), response);
    }

    /**
     * 일반 로그인 - 로그인
     * @param userDTO
     * @return
     */
    @Operation(summary = "일반 로그인")
    @PostMapping("/signin")
    public ApiResponse<UserAuthDto> authenticate(@RequestBody UserAuthDto userDTO) {

        UserAuthDto response = userService.signIn(userDTO.username(), userDTO.password());
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.USER_SIGNIN_SUCCESS.getMessage(), response);
    }
}
