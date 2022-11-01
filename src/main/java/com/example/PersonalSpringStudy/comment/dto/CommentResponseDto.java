package com.example.PersonalSpringStudy.comment.dto;

import com.example.PersonalSpringStudy.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long postId;
    private Long commentId;
    private String comments;
    private String email;

    public CommentResponseDto(Comment comment) {
        this.postId = comment.getPostId();
        this.commentId = comment.getId();
        this.comments = comment.getComments();
        this.email = comment.getEmail();


    }


}