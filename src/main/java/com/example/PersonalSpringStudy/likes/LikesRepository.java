package com.example.ReactSpringCollaborationProject.likes;

import com.example.ReactSpringCollaborationProject.account.service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes,Long> {
    Optional<Likes> findByAccountAndPostId(Account account, Long postId);

}
