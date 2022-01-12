package com.example.pocketmark.service;

import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.LoginDto;
import com.example.pocketmark.security.provider.TokenBox;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("AuthenticationService 테스트")
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;


    @DisplayName("UserService loginTest")
    @Test
    public void givenLoginReq_whenLogin_thenReturnTokenBox(){
        //Given
        LoginDto.LoginReq req = LoginDto.LoginReq.builder()
                .email("test@gmail.com")
                .pw("12345678")
                .build();

    }

}