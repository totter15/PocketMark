package com.example.pocketmark.domain;

import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.util.Encryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.Assertions.assertThat;


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


//        then(user.getId()).isEqualTo(1L);
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