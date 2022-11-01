package com.example.PersonalSpringStudy.post;

import com.example.PersonalSpringStudy.account.service.entity.Account;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long postId;
    private String title;
    private String contents;
    private String email;
    private String urlToString;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;

    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.email = post.getEmail();
        this.urlToString = post.getUrlToString();
        //this.nickname = post.
    }
    public PostResponseDto(Post post, Account account) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.email = post.getEmail();
        this.urlToString = post.getUrlToString();
        this.nickname = account.getNickname();
    }


}