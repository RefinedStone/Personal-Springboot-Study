package com.example.PersonalSpringStudy.comment.service;


import com.example.PersonalSpringStudy.account.entity.Account;
import com.example.PersonalSpringStudy.comment.dto.CommentRequestDto;
import com.example.PersonalSpringStudy.comment.dto.CommentResponseDto;
import com.example.PersonalSpringStudy.comment.entity.Comment;
import com.example.PersonalSpringStudy.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    //커멘트 생성
    public CommentResponseDto createComment(CommentRequestDto requestDto, Account account) {
        var r = new Comment(requestDto, account);
        commentRepository.save(r);
        return new CommentResponseDto(r);
    }

    // 커멘트 수정
    public CommentResponseDto updateComment(CommentRequestDto requestDto, Long id, Account account) {

        var r = commentRepository.findById(id).orElseThrow(
                () -> new RuntimeException("comment is not exist"));
        if (!account.getEmail().equals(r.getEmail())) {
            throw new RuntimeException("not matched email");
        }
        r.update(requestDto);
        return new CommentResponseDto(r);
    }

    //커멘트 삭제
    public String deleteComment(Long Id, Account account) {
        var r = commentRepository.findById(Id).orElseThrow(
                () -> new RuntimeException("comment is not exist"));
        if (!account.getEmail().equals(r.getEmail())) {
            throw new RuntimeException("not matched email");}
        commentRepository.deleteById(Id);
        return "Delete success";
    }
    public List<CommentResponseDto> getAllMyComments(Account account) {
        var commentLists = commentRepository.findAllByEmail(account.getEmail());
        var commentResponseLists = new ArrayList<CommentResponseDto>();
        for(Comment commentList: commentLists){
            commentResponseLists.add(new CommentResponseDto(commentList));
        }
        return commentResponseLists;
    }
    //커멘트 읽기
    public CommentResponseDto getOneComment(Long Id, Account account) {
        var r = commentRepository.findById(Id).orElseThrow(
                () -> new RuntimeException("comment is not exist")
        );
        if (!r.getEmail().equals(account.getEmail())) {
            throw new RuntimeException("email does not match");
        }
        return new CommentResponseDto(r);
    }
}