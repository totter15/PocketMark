package com.example.pocketmark.dto;

import com.example.pocketmark.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

class MyPageDtoTest {

    @DisplayName("User 객체를 받아와 MyPageDto 로 변환한다.")
    @Test
    public void givenUser_whenConvertDto_whenReturnMyPageDto(){
        //Given
        User user = User.builder()
                .email("test@gmail.com")
                .nickName("JyuKa")
                .pw("123445612")
                .build();

        //When
        MyPageDto dto = MyPageDto.fromUser(user);

        //Then
        then(dto.getEmail()).isEqualTo(user.getEmail());
        then(dto.getNickName()).isEqualTo(user.getNickName());
    }

}