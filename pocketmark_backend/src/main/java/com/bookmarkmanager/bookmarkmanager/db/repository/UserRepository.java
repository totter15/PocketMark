package com.bookmarkmanager.bookmarkmanager.db.repository;

import java.util.List;

import com.bookmarkmanager.bookmarkmanager.db.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    public List<User> findByUserId(String userId);
    public List<User> findByUserEmail(String email);
}
