package com.example.PersonalSpringStudy.post;


import com.example.PersonalSpringStudy.account.service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByAccount(Account account);
    Optional<Post> findById(Long Id);


}
