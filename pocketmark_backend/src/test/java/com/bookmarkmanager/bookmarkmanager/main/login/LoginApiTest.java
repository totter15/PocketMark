package com.bookmarkmanager.bookmarkmanager.main.login;

import com.bookmarkmanager.bookmarkmanager.db.login.User;
import com.bookmarkmanager.bookmarkmanager.db.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class LoginApiTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void userRepositoryTest(){
        User user = new User();
        user.setUserId("sim2626");
        user.setUserPw("252200");
        user.setEmail("sim2626@naver.com");
        userRepository.save(user);

        userRepository.findAll().forEach(System.out::println);

        User user1 = userRepository.findByUserId("hahaha");
        User user2 = userRepository.findByUserId("sim2626");

        
        log.info(""+user1);
        log.info(user2.toString());

    }

}
