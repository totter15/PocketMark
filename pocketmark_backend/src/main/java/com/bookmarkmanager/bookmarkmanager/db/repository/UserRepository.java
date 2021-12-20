package com.bookmarkmanager.bookmarkmanager.db.repository;

import java.util.List;

import com.bookmarkmanager.bookmarkmanager.db.entity.User;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto.SignUpReq;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findByUserId(String userId);
    public User findByUserEmail(String email);

    
}
