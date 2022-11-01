package com.example.PersonalSpringStudy.account.repository;


import com.example.PersonalSpringStudy.account.service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
}
