package com.example.pocketmark.repository;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.exception.GeneralException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.Option;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void initUser(){
        for (int i = 0; i < 10; i++) {
            User user = User.builder()
                    .email("test"+i+"@gmail.com")
                    .nickName("user"+i)
                    .pw("password"+i)
                    .build();
            userRepository.save(user);
        }
    }

    @AfterEach
    void deleteData(){
        userRepository.deleteAll();
    }

    @DisplayName("findByEmail 검증")
    @Test
    public void findByEmail(){
        User user = userRepository.findByEmail("test3@gmail.com")
                        .orElseThrow(EntityNotFoundException::new);

        then(user).isNotNull();
    }

    @DisplayName("findByNickName 검증")
    @Test
    public void findByNickName(){
        User user = userRepository.findByNickName("user8")
                .orElseThrow(EntityNotFoundException::new);

        then(user).isNotNull();
    }

    @DisplayName("이메일, 닉네임 중복검사")
    @Test
    public void findByNickNameOrEmail(){
        List<User> user = userRepository.findByNickNameOrEmail("user8","test3@gmail.com");

        then(user).hasSize(2);
    }

    @DisplayName("existsByNickName 검증")
    @Test
    public void existsByNickName(){
        boolean nickNameExist = userRepository.existsByNickName("user1");
        then(nickNameExist).isTrue();
    }
}