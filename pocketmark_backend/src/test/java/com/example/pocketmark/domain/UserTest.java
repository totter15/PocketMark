package com.example.pocketmark.domain;

import com.example.pocketmark.dto.UserDto.signUpRequest;
import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.service.UserService;
import com.example.pocketmark.util.Encryptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class UserTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    Encryptor encryptor;

    @PersistenceContext
    EntityManager em;

    // @AfterEach
    // public void deleteData(){
    //     userRepository.deleteAll();
    // }


    @DisplayName("createUser 테스트")
    @Test
    public void saveUser(){
        User newUser = createUser();
        userRepository.save(newUser);

        em.flush();
        em.clear();

        User user = userRepository.findById(newUser.getId())
                .orElseThrow(EntityNotFoundException::new);


        then(user.getId()).isEqualTo(1L);
        then(user.getEmail()).isEqualTo("test@gmail.com");
        then(user.getPw()).isEqualTo("1234");
        then(user.getCreatedAt()).isBefore(LocalDateTime.now());
        then(user.getUpdatedAt()).isBefore(LocalDateTime.now());
        then(user.isDeleted()).isFalse();

    }

    @DisplayName("비밀번호 암호화 테스트")
    @Test
    public void encryptorTest(){
        User user = new User(
                "test@gmail.com",
                encryptor.encrypt("1234"),
                "JyuKa");

        System.out.println(user.getPw());
        then(user.isMatch(encryptor,"1234")).isTrue();
    }

    
    

    public User createUser(){
        return new User("test@gmail.com","1234","JyuKa");
    }




}