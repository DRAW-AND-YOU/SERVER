package com.drawandyou.drawandyou_server.domain.article.presentation;

import com.drawandyou.drawandyou_server.domain.article.application.service.ArticleReadService;
import com.drawandyou.drawandyou_server.domain.article.application.service.ArticleService;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.request.ArticleCreateRequest;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.request.ArticleUpdateRequest;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.ArticleCreateResponse;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.ArticleDetailResponse;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.ArticleScrollResponse;
import com.drawandyou.drawandyou_server.domain.article.presentation.message.ResponseMessage;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "ARTICLE", description = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleReadService articleReadService;

    @Operation(summary = "게시글 생성하기")
    @PostMapping
    public ApiResponse<ArticleCreateResponse> createArticle(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid ArticleCreateRequest request){

        ArticleCreateResponse response = articleService.createArticle(userId, request);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.ARTICLE_CREATE_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "게시글 상세조회하기")
    @GetMapping("/{articleId}")
    public ApiResponse<ArticleDetailResponse> getArticle(
            @PathVariable Long articleId) {

        ArticleDetailResponse response = articleReadService.getArticle(articleId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.ARTICLE_DETAIL_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "게시글 수정하기")
    @PatchMapping("/{articleId}")
    public ApiResponse<Void> updateArticle(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long articleId,
            @RequestBody @Valid ArticleUpdateRequest request) {

        articleService.updateArticle(userId, articleId, request);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.ARTICLE_UPDATE_SUCCESS.getMessage());
    }

    @Operation(summary = "게시글 삭제하기")
    @DeleteMapping("/{articleId}")
    public ApiResponse<Void> deleteArticle(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long articleId) {

        articleService.deleteArticle(userId, articleId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.ARTICLE_DELETE_SUCCESS.getMessage(), null);
    }

    @Operation(summary = "최신 게시글 리스트 조회(무한스크롤)")
    @GetMapping("/infinite-scrolls")
    public ApiResponse<ArticleScrollResponse> getArticlesInfiniteScroll(
            @RequestParam(defaultValue = "20") Long limit,
            @RequestParam(required = false) LocalDateTime lastCreatedAt,
            @RequestParam(required = false) Long lastArticleId
    ) {
        ArticleScrollResponse response = articleService.readAllInfiniteScroll(lastCreatedAt, limit, lastArticleId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.ARTICLE_SCROLL_SUCCESS.getMessage(), response);
    }
}
