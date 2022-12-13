package com.example.PersonalSpringStudy.account.repository;


import com.example.PersonalSpringStudy.account.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByAccountEmail(String email);
    //RefreshToken findByAccountEmail(String email);
}
