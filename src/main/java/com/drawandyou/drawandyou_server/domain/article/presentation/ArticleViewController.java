package com.drawandyou.drawandyou_server.domain.article.presentation;

import com.drawandyou.drawandyou_server.domain.article.application.ArticleViewService;
import com.drawandyou.drawandyou_server.domain.article.presentation.dto.response.ArticleViewCountResponse;
import com.drawandyou.drawandyou_server.domain.article.presentation.message.ResponseMessage;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

@Tag(name = "ARTICLE VIEWS", description = "게시글 조회수 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article-views")
public class ArticleViewController {

    private final ArticleViewService articleViewService;

    @Operation(summary = "게시글 조회수 증가")
    @PostMapping("/articles/{articleId}")
    public ApiResponse<ArticleViewCountResponse> increase(@PathVariable Long articleId){
        ArticleViewCountResponse response = articleViewService.increase(articleId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.ARTICLE_VIEW_COUNT_INCREASEMENT_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "게시글 조회수 조회")
    @GetMapping("/articles/{articleId}/count")
    public ApiResponse<ArticleViewCountResponse> count(@PathVariable Long articleId){
        ArticleViewCountResponse response = articleViewService.count(articleId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.ARTICLE_VIEW_COUNT_GET_SUCCESS.getMessage(), response);
    }
}
