package com.example.pocketmark.repository;

import com.example.pocketmark.domain.EmailAuthenticationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthenticationCodeRepository extends JpaRepository<EmailAuthenticationCode, Long> {

    EmailAuthenticationCode findFirstByEmailOrderByCreatedAtDesc(String email);
}
