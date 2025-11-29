package com.drawandyou.drawandyou_server.domain.comment.application.service;

import com.drawandyou.drawandyou_server.domain.comment.domain.entity.Comment;
import com.drawandyou.drawandyou_server.domain.comment.domain.repository.CommentRepository;
import com.drawandyou.drawandyou_server.domain.comment.exception.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentFindService {

    private final CommentRepository commentRepository;

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }
}