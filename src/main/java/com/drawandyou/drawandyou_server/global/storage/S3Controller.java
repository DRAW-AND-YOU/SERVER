package com.drawandyou.drawandyou_server.global.storage;

import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import com.drawandyou.drawandyou_server.global.storage.application.service.S3Service;
import com.drawandyou.drawandyou_server.global.storage.presentation.dto.request.PresignedUrlCreateRequest;
import com.drawandyou.drawandyou_server.global.storage.presentation.dto.response.PresignedUrlResponse;
import com.drawandyou.drawandyou_server.global.storage.presentation.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "S3", description = "S3 관련 API")
@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @Operation(summary = "S3 Presigned URL 발급")
    @PostMapping("/presigned-url")
    public ApiResponse<PresignedUrlResponse> getS3PresignedUrl(@RequestBody PresignedUrlCreateRequest presignedUrlCreateRequest){
        PresignedUrlResponse response = s3Service.createPresignedUrl(presignedUrlCreateRequest);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.PRESIGNED_URL_CREATE_SUCCESS.getMessage(), response);
    }


}
