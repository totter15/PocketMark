package com.bookmarkmanager.bookmarkmanager.db.repository;

import com.bookmarkmanager.bookmarkmanager.db.login.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findByUserId(String userId);
    public User findByEmail(String email);
}
