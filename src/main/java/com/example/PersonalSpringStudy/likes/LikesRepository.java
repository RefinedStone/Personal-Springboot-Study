package com.example.PersonalSpringStudy.likes;

import com.example.PersonalSpringStudy.account.service.entity.Account;
import com.example.PersonalSpringStudy.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes,Long> {
    Optional<Likes> findByAccountAndPostId(Account account, Long postId);
    Optional<Likes> findByAccountAndPost(Account account, Post post);

}
