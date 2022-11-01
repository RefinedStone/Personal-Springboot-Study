package com.example.PersonalSpringStudy.comment.repository;

import com.example.PersonalSpringStudy.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByOrderByCreatedAtDesc();
    Comment findById(String Id);
   // Comment findBy
    List<Comment> findAllByEmail(String email);
}