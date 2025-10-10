package com.drawandyou.drawandyou_server.user.presentation;

import com.drawandyou.drawandyou_server.global.auth.presentation.dto.UserAuthDto;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import com.drawandyou.drawandyou_server.global.security.TokenProvider;
import com.drawandyou.drawandyou_server.user.application.service.UserService;
import com.drawandyou.drawandyou_server.user.domain.entity.User;
import com.drawandyou.drawandyou_server.user.presentation.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 일반 로그인 - 회원가입
     * @param userDTO
     * @return
     */
    @Operation(summary = "일반 회원가입")
    @PostMapping("/signup")
    public ApiResponse<UserAuthDto> registerUser(@RequestBody UserAuthDto userDTO) {

        if (userDTO == null || userDTO.password() == null) {
            throw new RuntimeException("Invalid Password value.");
        }

        User user = User.builder()
                .username(userDTO.username())
                .password(passwordEncoder.encode(userDTO.password()))
                .build();

        User registeredUser = userService.create(user);
        UserAuthDto responseUserDTO = UserAuthDto.builder()
                .id(registeredUser.getId())
                .username(registeredUser.getUsername())
                .build();

        return ApiResponse.success(HttpStatus.OK, ResponseMessage.USER_SIGNUP_SUCCESS.getMessage(), responseUserDTO);
    }

    /**
     * 일반 로그인 - 로그인
     * @param userDTO
     * @return
     */
    @Operation(summary = "일반 로그인")
    @PostMapping("/signin")
    public ApiResponse<UserAuthDto> authenticate(@RequestBody UserAuthDto userDTO) {
        User user = userService.getByCredentials(userDTO.username(), userDTO.password(), passwordEncoder);

        // 인증 성공시 jwt 토큰 발급
        final String token = tokenProvider.create(user);

        // 응답 객체에 사용자 정보 및 토큰 포함 (비밀번호 같은 민감 정보 포함 x)
        final UserAuthDto responseUserDTO = UserAuthDto.builder()
                .username(user.getUsername())
                .id(user.getId())
                .token(token)
                .build();

        return ApiResponse.success(HttpStatus.OK, ResponseMessage.USER_SIGNIN_SUCCESS.getMessage(), responseUserDTO);
    }
}
