package com.example.pocketmark.service;

import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("LoginService 테스트")
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserService userService;

    @DisplayName("UserService SignUpTest")
    @Test
    public void givenSignUpDto_whenCreateUser_thenSuccess(){
        //Given
        given(userService.create(any()))
                .willReturn(User.builder()
                        .email("test@gmail.com")
                        .nickName("JyuKa")
                        .pw("1234567487")
                        .build()
                );

        UserDto.SignUpDto dto =
                UserDto.SignUpDto.builder()
                        .email("test@gmail.com")
                        .nickName("JyuKa")
                        .pw("1234567487")
                        .build()
                ;

        //When
        loginService.signUp(dto);

        //Then
    }

}