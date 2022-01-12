package com.example.pocketmark.service;

import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.LoginDto;
import com.example.pocketmark.dto.SignUpUserDto;
import com.example.pocketmark.security.provider.TokenBox;
import com.example.pocketmark.util.Encryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@DisplayName("LoginService 테스트")
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserService userService;

    @Mock
    private Encryptor encryptor;


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

        SignUpUserDto.SignUpDto dto =
                SignUpUserDto.SignUpDto.builder()
                        .email("test@gmail.com")
                        .nickName("JyuKa")
                        .pw("1234567487")
                        .build()
                ;

        //When & Then
        loginService.signUp(dto);
    }

}