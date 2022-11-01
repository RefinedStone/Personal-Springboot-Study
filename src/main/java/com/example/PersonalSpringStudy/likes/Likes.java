package com.example.PersonalSpringStudy.likes;

import com.example.PersonalSpringStudy.account.service.entity.Account;
import com.example.PersonalSpringStudy.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @Column(nullable = true)
    private Boolean likeCheck;

    //연관관계
    // Many Likes To One Post
    @ManyToOne
    @JoinColumn(name = "post", nullable = true)
    private Post post;

    //연관관계
    // Many Likes To One Account
    @ManyToOne
    @JoinColumn(name = "account", nullable = true)
    private Account account;

    public Likes(Account account, Post post) {
        this.account = account;
        this.post = post;
        this.likeCheck = true;
    }

    public Boolean getLikeCheck() {
        return this.likeCheck;
    }

    public Boolean setLikeCheck(Boolean likeCheck) {
        this.likeCheck = likeCheck;
        return this.likeCheck;
    }

}
