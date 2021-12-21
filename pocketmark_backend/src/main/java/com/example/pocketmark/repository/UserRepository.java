package com.example.pocketmark.repository;

import com.example.pocketmark.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
