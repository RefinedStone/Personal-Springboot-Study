package com.example.ReactSpringCollaborationProject.post;


import com.example.ReactSpringCollaborationProject.Timestamped;
import com.example.ReactSpringCollaborationProject.account.service.entity.Account;
import com.example.ReactSpringCollaborationProject.comment.entity.Comment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Map;


@NoArgsConstructor
@Setter
@Getter
@Entity
public class Post extends Timestamped {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = true)
    private String title;
    @Column(nullable = true)
    private String contents;
    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String urlToString;

    @JsonIgnore
    @Column(nullable = true)
    private String urlKey;

    @JsonIgnore //JPA 순환참조
    @ManyToOne
    @JoinColumn(name = "account_Id")
    private Account account;

    //One post to Many comment
    @OneToMany(mappedBy = "post")
    private List<Comment> comment;

    public Post(String contents, String title) {
        this.contents = contents;
        this.title = title;
    }

    public Post(PostRequestDto requestDto) {
        this.contents = requestDto.getContents();
        this.title = requestDto.getTitle();
    }


//    public Post(PostRequestDto requestDto, Account account,Map<String,String> urlMap) {
//        this.contents = requestDto.getContents();
//        this.title = requestDto.getTitle();
//        this.account = account;
//        this.email = account.getEmail();
//        this.urlToString = urlMap.get("url");
//        this.urlKey = urlMap.get("key");
//    }
    public Post(String contents, Account account,Map<String,String> urlMap) {
        this.contents = contents;
        this.title = "";
        this.account = account;
        this.email = account.getEmail();
        this.urlToString = urlMap.get("url");
        this.urlKey = urlMap.get("key");
    }

    public Post(PostRequestDto requestDto, Account account) {
        this.contents = requestDto.getContents();
        this.title = requestDto.getTitle();
        this.account = account;
        this.email = account.getEmail();
    }
    public Post(String contents, Account account) {
        this.contents = contents;
        this.title = "";
        this.account = account;
        this.email = account.getEmail();
    }

    public Post(PostRequestDto requestDto, Map<String,String> urlMap) {
        this.contents = requestDto.getContents();
       // this.title = requestDto.getTitle();
        this.urlToString = urlMap.get("url");
        this.urlKey = urlMap.get("key");
    }

    public void update(PostRequestDto requestDto) {
        this.contents = requestDto.getContents();
     //   this.title = requestDto.getTitle();
    }
}
