package com.drawandyou.drawandyou_server.user.presentation;

import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import com.drawandyou.drawandyou_server.user.presentation.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/")
    public ApiResponse<Void> test(){
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.TEST_SUCCESS.getMessage());
    }

}
