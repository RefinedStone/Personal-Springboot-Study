package com.example.PersonalSpringStudy.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long postId;
    private String comments;
}