package com.example.ReactSpringCollaborationProject.post;

import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long postId;
    private String title;
    private String contents;
    private String email;
    private String urlToString;

    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.email = post.getEmail();
        this.urlToString = post.getUrlToString();
    }


}