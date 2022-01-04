package com.example.pocketmark.repository;

import com.example.pocketmark.domain.SnsUser;
import com.google.common.base.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SnsUserRepository  extends JpaRepository<SnsUser, Long> {
    
    Optional<SnsUser> findByoauth2UserId(String oauth2UserId);

}
