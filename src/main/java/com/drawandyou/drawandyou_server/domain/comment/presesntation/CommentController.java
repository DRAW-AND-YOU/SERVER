package com.drawandyou.drawandyou_server.domain.comment.presesntation;

import com.drawandyou.drawandyou_server.domain.comment.application.service.CommentService;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.message.ResponseMessage;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.request.CommentCreateRequest;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.response.CommentPageResponse;
import com.drawandyou.drawandyou_server.domain.comment.presesntation.response.CommentResponse;
import com.drawandyou.drawandyou_server.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "COMMENT", description = "댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public ApiResponse<CommentResponse> read(@PathVariable Long commentId){
        CommentResponse response = commentService.read(commentId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.COMMENT_READ_SUCCESS.getMessage(), response);
    }

    @PostMapping
    public ApiResponse<CommentResponse> create(@RequestBody CommentCreateRequest request){
        CommentResponse response = commentService.create(request);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.COMMENT_CREATE_SUCCESS.getMessage(), response);
    }

    @DeleteMapping("{commentId}")
    public ApiResponse<Void> delete(@PathVariable Long commentId){
        commentService.delete(commentId);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.COMMENT_DELETE_SUCCESS.getMessage());
    }

    @GetMapping
    public ApiResponse<CommentPageResponse >readAll(
            @RequestParam Long articleId,
            @RequestParam Long page,
            @RequestParam Long size){
        CommentPageResponse response = commentService.readAll(articleId, page, size);
        return ApiResponse.success(HttpStatus.OK, ResponseMessage.COMMENT_PAGINATION_GET_SUCCESS.getMessage(), response);
    }

}
