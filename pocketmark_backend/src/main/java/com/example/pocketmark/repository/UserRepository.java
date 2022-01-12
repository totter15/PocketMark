package com.example.pocketmark.repository;


import com.example.pocketmark.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickName(String nickName);
    List<User> findByNickNameOrEmail(String nickName, String email);
    Boolean existsByNickName(String nickName);
    Optional<User> findByEmailAndPw(String email, String pw);
    Boolean existsByEmail(String email);
}
