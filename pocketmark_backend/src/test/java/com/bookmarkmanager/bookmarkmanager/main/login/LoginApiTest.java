package com.bookmarkmanager.bookmarkmanager.main.login;

import java.util.List;

import com.bookmarkmanager.bookmarkmanager.db.entity.User;
import com.bookmarkmanager.bookmarkmanager.db.repository.UserRepository;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto.LoginReq;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto.SignUpReq;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class LoginApiTest {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;



    @Test
    @DisplayName("SignUp & Login Test")
    public void AccountTest() throws Exception{

        String signUpUrl = "/api/sign-up";
        String loginUrl = "/api/login";
        
        objectMapper= Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .build();
        
        SignUpReq signUpReq = UserDto.SignUpReq.builder()
                            .userId("sim2280")
                            .userPw("1234")
                            .userEmail("daniel@naver.com")
                            .build();

        String content = objectMapper.writeValueAsString(signUpReq);
        log.info(content);

        System.out.println("# 회원가입 테스트");
        mockMvc
        .perform(MockMvcRequestBuilders.post(signUpUrl)
        .content(content)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect( result -> {
            MockHttpServletResponse response = result.getResponse();
            log.info(response.getContentAsString());
        });
        
        System.out.println("# 회원가입 후 DB 확인");
        userRepository.findAll().forEach(System.out::println);
        
        LoginReq loginReq = UserDto.LoginReq.builder().userId("sim2280").userPw("1234").build();
        content = objectMapper.writeValueAsString(loginReq);
        log.info("#loginReq : {}",content);

        
        System.out.println("# 로그인 테스트");
        mockMvc
        .perform(MockMvcRequestBuilders.post(loginUrl)
        .content(content)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("true"));
        
        
    }


    @Test
    public void userRepositoryTest(){
        User user = new User("sim2626","1234","sim2727@naver.com");
        userRepository.save(user);
        user = User.builder()
            .userId("q8429")
            .userPw("252200")
            .userEmail("www@userEmail")
            .build();
        userRepository.save(user);

        userRepository.findAll().forEach(System.out::println);

        List<User> user1 = userRepository.findByUserId("hahaha");
        List<User> user2 = userRepository.findByUserId("sim2626");

        
        log.info(""+user1);
        log.info(user2.toString());

    }

    @Test
    public void referenceTest(){
        User user = User.builder()
            .userId("q8429")
            .userPw("252200")
            .userEmail("www@userEmail")
            .build();

        User user2 = user;
        System.out.println("#user1");
        System.out.println(user);
        System.out.println("#user2");
        System.out.println(user2);

        System.out.println("data changed");
        user2.setDeleted(true);

        System.out.println("#user1");
        System.out.println(user);
        System.out.println("#user2");
        System.out.println(user2);

    }

}
