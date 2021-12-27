package com.example.pocketmark.service;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.UserDto;
import com.example.pocketmark.dto.UserDto.signUpRequest;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.util.Encryptor;
import org.apache.tomcat.jni.Error;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;


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
    public void givenNotOverlapNickNameAndEmail_whenSaveUser_thenReturnUser() throws SQLException{
        //Given
        UserDto.signUpRequest request = createSignUpRequest();

        given(userRepository.save(any()))
                .willReturn(createUser(request));

        //When
        User user = userService.create(UserDto.SignUpDto.fromSignUpRequest(request));


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
        Throwable thrown = catchThrowable(()->userService.create(UserDto.SignUpDto.fromSignUpRequest(request)));

        //Then
        then(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.NICKNAME_EXIST.getMessage());
        verify(userRepository).findByNickName(any());
    }

    @DisplayName("중복된 이메일이나 닉네임을 저장하려하면 DataIntegrityViolationException 에러를 발생시킨다. - unique 제약조건 테스트")
    @Test
    public void givenOverlapEmailOrNickName_whenSaveUser_thenReturnException(){
        //Given
        DataIntegrityViolationException e = new DataIntegrityViolationException("...");
        UserDto.signUpRequest request = createSignUpRequest();
        given(userRepository.save(any()))
                .willThrow(e);


        //When
        Throwable thrown = catchThrowable(()->userService.create(request));

        //Then
        then(thrown)
                .isInstanceOf(DataIntegrityViolationException.class);
        verify(userRepository).save(any()); 

    }

    @DisplayName("중복뒨 이메일로 가입을 시도할때 오류를 발생시킨다.")
    @Test
    public void givenOverlapEmail_whenSaveUser_thenReturnException(){
        //Given
        GeneralException e = new GeneralException(ErrorCode.EMAIL_EXIST);
        UserDto.signUpRequest request = createSignUpRequest();
        given(userRepository.findByEmail(any()))
                .willThrow(e);

        //When
        Throwable thrown = catchThrowable(()->userService.create(UserDto.SignUpDto.fromSignUpRequest(request)));

        //Then
        then(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.EMAIL_EXIST.getMessage());
        verify(userRepository).findByEmail(any());

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