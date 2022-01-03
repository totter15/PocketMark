package com.example.pocketmark.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class UserDtoTest {

    @DisplayName("UserDto.signUpRequest 를 UserDto.SignUpDto 로 변환하는 메소드 검증")
    @Test
    public void givenSignUpRequest_whenConvertSignUpDto_thenReturnSignUpDto(){
        //Given
        SignUpUserDto.signUpRequest request = SignUpUserDto.signUpRequest.builder()
                .email("test@gmail.com")
                .pw("1234")
                .nickName("JyuKa")
                .build();

        //When
        SignUpUserDto.SignUpDto dto = SignUpUserDto.SignUpDto.fromSignUpRequest(request);

        //Then
        then(dto.getEmail()).isEqualTo(request.getEmail());
        then(dto.getPw()).isEqualTo(request.getPw());
        then(dto.getNickName()).isEqualTo(request.getNickName());
    }

}