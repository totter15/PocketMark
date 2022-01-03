package com.example.pocketmark.service;

import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.SignUpUserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("LoginService 테스트")
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserService userService;

    MockHttpSession session;

    @DisplayName("UserService SignUpTest")
    @Test
    public void givenSignUpDto_whenCreateUser_thenSuccess(){
        //Given
        session = new MockHttpSession();

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

        //When
        loginService.signUp(dto,session);

        //Then
    }

}