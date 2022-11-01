package com.example.PersonalSpringStudy.comment.controller;

import com.example.PersonalSpringStudy.comment.dto.CommentRequestDto;
import com.example.PersonalSpringStudy.comment.service.CommentService;
import com.example.PersonalSpringStudy.global.dto.ResponseDto;
import com.example.PersonalSpringStudy.security.user.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/comment")
@RestController
public class CommentController {
    private final CommentService commentService;

    @Autowired
    CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글쓰기 api
    @PostMapping("/")
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(commentService.createComment(requestDto, userDetails.getAccount()));
    }

    // 댓글수정 api
    @PutMapping("/{id}")
    public ResponseDto<?> updateComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(commentService.updateComment(requestDto, id, userDetails.getAccount()));
    }

    // 댓글삭제 api
    @DeleteMapping("/{id}")
    public ResponseDto<?> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(commentService.deleteComment(id,userDetails.getAccount()));
    }

    //댓글 1개 읽기 api
    @GetMapping("/{id}")
    public ResponseDto<?> getOneComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(commentService.getOneComment(id,userDetails.getAccount()));
    }

    //내 모든 comments 보여주기
    @GetMapping("/my")
    public ResponseDto<?> getAllMyComments(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(commentService.getAllMyComments(userDetails.getAccount()));
    }
}
