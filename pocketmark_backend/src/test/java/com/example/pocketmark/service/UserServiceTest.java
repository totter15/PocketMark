package com.example.pocketmark.service;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.UserDto;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.util.Encryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@DisplayName("UserService 테스트")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private Encryptor encryptor;

    @Mock
    private UserRepository userRepository;

    @DisplayName("아무런 중복이 없는 조건을 입력하여 유저를 저장한다")
    @Test
    public void givenNotOverlapNickNameAndEmail_whenSaveUser_thenReturnUser(){
        //Given
        UserDto.signUpRequest request = createSignUpRequest();

        given(userRepository.save(any()))
                .willReturn(createUser(request));

        //When
        User user = userService.create(request);


        //Then
        then(user.getEmail()).isEqualTo(request.getEmail());
        then(user.getPw()).isEqualTo(request.getPw());
        then(user.getNickName()).isEqualTo(request.getNickName());
        verify(userRepository).save(any());
    }

    @DisplayName("중복된 닉네임으로 가입을 시도할떄 오류를 발생시킨다.")
    @Test
    public void givenOverlapNickName_whenSaveUser_thenReturnException(){
        //Given
        GeneralException e = new GeneralException(ErrorCode.NICKNAME_EXIST);
        UserDto.signUpRequest request = createSignUpRequest();
        given(userRepository.findByNickName(any()))
                .willThrow(e);

        //When
        Throwable thrown = catchThrowable(()->userService.create(request));

        //Then
        then(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.NICKNAME_EXIST.getMessage());
        verify(userRepository).findByNickName(any());


    }

    public User createUser(UserDto.signUpRequest request){

        return User.builder()
                .email(request.getEmail())
                .pw(request.getPw())
                .nickName(request.getNickName())
                .build();
    }

    public UserDto.signUpRequest createSignUpRequest(){


        return UserDto.signUpRequest
                .builder()
                .email("test@test.com")
                .pw("1234")
                .nickName("user1")
                .build();
    }


}