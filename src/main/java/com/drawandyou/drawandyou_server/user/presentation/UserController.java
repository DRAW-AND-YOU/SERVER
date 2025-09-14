package com.drawandyou.drawandyou_server.user.presentation;

import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import com.drawandyou.drawandyou_server.user.presentation.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequiredArgsConstructor
public class UserController {

    @Operation(summary = "API 테스트", description = "API 서버 연결 상태를 확인하는 테스트 엔드포인트입니다.")
    @GetMapping("/")
    public ApiResponse<Void> test(){
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.TEST_SUCCESS.getMessage());
    }

}
