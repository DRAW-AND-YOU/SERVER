package com.drawandyou.drawandyou_server.image.presentation;

import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import com.drawandyou.drawandyou_server.image.application.service.S3Service;
import com.drawandyou.drawandyou_server.image.presentation.dto.response.ImageUploadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Image", description = "이미지 업로드 관련 API")
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final S3Service s3Service;

    @Operation(summary = "이미지 업로드", description = "S3에 이미지를 업로드합니다.")
    @PostMapping("/upload")
    public ApiResponse<ImageUploadResponse> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "images") String folder) {

        String imageUrl = s3Service.uploadImage(file, folder);
        ImageUploadResponse response = new ImageUploadResponse(imageUrl);

        return ApiResponse.success(HttpStatus.OK, "이미지 업로드 성공", response);
    }

    @Operation(summary = "이미지 삭제", description = "S3에서 이미지를 삭제합니다.")
    @DeleteMapping
    public ApiResponse<Void> deleteImage(@RequestParam("url") String fileUrl) {
        s3Service.deleteImage(fileUrl);
        return ApiResponse.success(HttpStatus.OK, "이미지 삭제 성공");
    }
}
