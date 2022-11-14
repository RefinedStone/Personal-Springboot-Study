package com.example.PersonalSpringStudy.comment.entity;

import com.example.PersonalSpringStudy.Timestamped;
import com.example.PersonalSpringStudy.account.service.entity.Account;
import com.example.PersonalSpringStudy.comment.dto.CommentRequestDto;
import com.example.PersonalSpringStudy.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class Comment extends Timestamped {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String comments;

    // many comment to one post.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post")
    private Post post;

    // many comment to one account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account")
    private Account account;

    public Comment(CommentRequestDto requestDto) {
        this.comments = requestDto.getComments();
    }

    public Comment(Post post,CommentRequestDto requestDto, Account account) {
        this.comments = requestDto.getComments();
        this.post = post;
        this.account = account;
    }

    public void update(CommentRequestDto requestDto) {
        this.comments = requestDto.getComments();
    }
}
