package com.example.ReactSpringCollaborationProject.comment.entity;

import com.example.ReactSpringCollaborationProject.Timestamped;
import com.example.ReactSpringCollaborationProject.account.service.entity.Account;
import com.example.ReactSpringCollaborationProject.comment.dto.CommentRequestDto;
import com.example.ReactSpringCollaborationProject.post.Post;
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

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "post")
    private Post post;

    public Comment(CommentRequestDto requestDto) {
        this.comments = requestDto.getComments();
    }

    public Comment(CommentRequestDto requestDto, Account account) {
        this.comments = requestDto.getComments();
        this.postId = requestDto.getPostId();
        this.email = account.getEmail();
    }

    public void update(CommentRequestDto requestDto) {
        this.comments = requestDto.getComments();
    }
}
