package com.drawandyou.drawandyou_server.domain.like.presentation;

import com.drawandyou.drawandyou_server.domain.like.application.service.ArticleLikeService;
import com.drawandyou.drawandyou_server.domain.like.presentation.dto.response.ArticleLikeResponse;
import com.drawandyou.drawandyou_server.domain.like.presentation.dto.response.LikeCountResponse;
import com.drawandyou.drawandyou_server.domain.like.presentation.message.ResponseMessage;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ARTICLE LIKE", description = "게시글 좋아요 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article-likes")
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @Operation(summary = "게시글 좋아요 조회")
    @GetMapping("/articles/{articleId}")
    public ApiResponse<ArticleLikeResponse> read(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long articleId
    ){
        ArticleLikeResponse response = articleLikeService.read(articleId, userId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.ARTICLE_LIKE_GET_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "게시글에 좋아요 누르기")
    @PostMapping("/articles/{articleId}")
    public ApiResponse<Void> like(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long articleId
    ){
        articleLikeService.likePessimisticLock(articleId, userId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.ARTICLE_LIKE_SUCCESS.getMessage());
    }

    @Operation(summary = "게시글 좋아요 취소하기")
    @DeleteMapping("/articles/{articleId}")
    public ApiResponse<Void> unlike(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long articleId
    ){
        articleLikeService.unlikePessimisticLock(articleId, userId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.ARTICLE_UNLIKE_SUCCESS.getMessage());
    }

    @Operation(summary = "특정 게시글의 좋아요 수 조회하기")
    @GetMapping("/articles/{articleId}/count")
    public ApiResponse<LikeCountResponse> count(@PathVariable Long articleId) {
        LikeCountResponse count = articleLikeService.count(articleId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.ARTICLE_LIKE_COUNT_GET_SUCCESS.getMessage(), count);
    }
}
