package com.drawandyou.drawandyou_server.domain.comment.presesntation;

import com.drawandyou.drawandyou_server.domain.comment.application.service.CommentService;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.message.ResponseMessage;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.request.CommentCreateRequest;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.response.CommentCountResponse;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.response.CommentPageResponse;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.response.CommentResponse;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "COMMENT", description = "댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 단건 조회")
    @GetMapping("/{commentId}")
    public ApiResponse<CommentResponse> read(
            @PathVariable Long commentId,
            @AuthenticationPrincipal Long userId){
        CommentResponse response = commentService.read(commentId, userId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.COMMENT_READ_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "댓글 작성하기")
    @PostMapping
    public ApiResponse<CommentResponse> create(
            @AuthenticationPrincipal Long userId,
            @RequestBody CommentCreateRequest request){
        CommentResponse response = commentService.create(userId, request);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.COMMENT_CREATE_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "댓글 삭제하기")
    @DeleteMapping("{commentId}")
    public ApiResponse<Void> delete(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long commentId){
        commentService.delete(userId, commentId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.COMMENT_DELETE_SUCCESS.getMessage());
    }

    @Operation(summary = "댓글 목록 페이지네이션 조회")
    @GetMapping
    public ApiResponse<CommentPageResponse> readAll(
            @RequestParam Long articleId,
            @RequestParam Long page,
            @RequestParam Long size,
            @AuthenticationPrincipal Long userId){
        CommentPageResponse response = commentService.readAll(articleId, page, size, userId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.COMMENT_PAGINATION_GET_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "특정 게시글의 댓글 수 조회하기")
    @GetMapping("/articles/{articleId}/count")
    public ApiResponse<CommentCountResponse> count(@PathVariable Long articleId) {
        CommentCountResponse count = commentService.count(articleId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.COMMENT_COUNT_GET_SUCCESS.getMessage(), count);
    }

}
