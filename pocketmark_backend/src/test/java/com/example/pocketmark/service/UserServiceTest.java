package com.example.pocketmark.service;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.ModifyPwDto;
import com.example.pocketmark.dto.SignUpUserDto;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.util.Encryptor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockHttpSession;

import javax.swing.text.html.Option;
import java.sql.SQLException;
import java.util.Optional;

import static com.example.pocketmark.controller.api.UserApiController.LOGIN_SESSION_KEY;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.MatcherAssert.assertThat;
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

    MockHttpSession session;

    @DisplayName("아무런 중복이 없는 조건을 입력하여 유저를 저장한다")
    @Test
    public void givenNotOverlapNickNameAndEmail_whenSaveUser_thenReturnUser() throws SQLException{
        //Given
        SignUpUserDto.signUpRequest request = createSignUpRequest();

        given(userRepository.save(any()))
                .willReturn(createUser(request));

        //When
        User user = userService.create(SignUpUserDto.SignUpDto.fromSignUpRequest(request));


        //Then
        then(user.getEmail()).isEqualTo(request.getEmail());
        then(user.getPw()).isEqualTo(request.getPw());
        then(user.getNickName()).isEqualTo(request.getNickName());
        verify(userRepository).save(any());
    }

    @Deprecated
    @Disabled("DataIntegrityViolationException 추가로 인한 비활성화")
    @DisplayName("중복된 닉네임으로 가입을 시도할떄 오류를 발생시킨다.")
    @Test
    public void givenOverlapNickName_whenSaveUser_thenReturnException(){
        //Given
        GeneralException e = new GeneralException(ErrorCode.NICKNAME_EXIST);
        SignUpUserDto.signUpRequest request = createSignUpRequest();
        given(userRepository.findByNickName(any()))
                .willThrow(e);

        //When
        Throwable thrown = catchThrowable(()->userService.create(SignUpUserDto.SignUpDto.fromSignUpRequest(request)));

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
        SignUpUserDto.signUpRequest request = createSignUpRequest();
        SignUpUserDto.SignUpDto dto = SignUpUserDto.SignUpDto.fromSignUpRequest(request);
        given(userRepository.save(any()))
                .willThrow(e);


        //When
        Throwable thrown = catchThrowable(()->userService.create(dto));

        //Then
        then(thrown)
                .isInstanceOf(DataIntegrityViolationException.class);
        verify(userRepository).save(any()); 

    }

    @Deprecated
    @Disabled("DataIntegrityViolationException 추가로 인한 비활성화")
    @DisplayName("중복뒨 이메일로 가입을 시도할때 오류를 발생시킨다.")
    @Test
    public void givenOverlapEmail_whenSaveUser_thenReturnException(){
        //Given
        GeneralException e = new GeneralException(ErrorCode.EMAIL_EXIST);
        SignUpUserDto.signUpRequest request = createSignUpRequest();
        given(userRepository.findByEmail(any()))
                .willThrow(e);

        //When
        Throwable thrown = catchThrowable(()->userService.create(SignUpUserDto.SignUpDto.fromSignUpRequest(request)));

        //Then
        then(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.EMAIL_EXIST.getMessage());
        verify(userRepository).findByEmail(any());

    }

    @DisplayName("LOGIN_SESSION_KEY 를 받아 유저정보를 조회한다.")
    @Test
    public void givenHttpSession_whenSelectUser_thenReturnUser(){
        //Given
        session = new MockHttpSession();
        session.setAttribute(LOGIN_SESSION_KEY,1L);
        given(userRepository.findById(any()))
                .willReturn(
                        Optional.of(User.builder()
                                .email("test@gmail.com")
                                .pw("12341234")
                                .nickName("JyuKa")
                                .build()
                        )
                );

        //When
        User user = userService.selectUser(session);

        //Then
        then(user.getEmail()).isEqualTo("test@gmail.com");
        then(user.getNickName()).isEqualTo("JyuKa");
        then(user.getPw()).isEqualTo("12341234");
        verify(userRepository).findById(any());
    }

    @DisplayName("비어있는 LOGIN_SESSION_KEY 를 받아 Exception 을 발생시킨다.")
    @Test
    public void givenNullHttpSession_whenSelectUser_thenReturnUnAuthorizeException(){
        //Given
        session = new MockHttpSession();
        session.setAttribute(LOGIN_SESSION_KEY,null);

        //When
        Throwable thrown = catchThrowable(()->userService.selectUser(session));

        //Then
        then(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.UNAUTHORIZED.getMessage());
    }


    @DisplayName("존재하지 않는 LOGIN_SESSION_KEY 를 받아 ENTITY_NOT_EXIST Exception 을 발생시킨다.")
    @Test
    public void givenNullHttpSession_whenSelectUser_thenReturnException(){
        //Given
        session = new MockHttpSession();
        session.setAttribute(LOGIN_SESSION_KEY,3L);
        given(userRepository.findById(any()))
                .willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(()->userService.selectUser(session));

        //Then
        then(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.ENTITY_NOT_EXIST.getMessage());
        verify(userRepository).findById(any());
    }

    @DisplayName("정상적인 비밀번호 변경")
    @Test
    public void givenChangePwDto_whenChangePw_thenReturnChangePw(){
        //Given
        ModifyPwDto.ChangePwDto changePwDto = ModifyPwDto.ChangePwDto.builder()
                .nowPw("1234").newPw("4321").confPw("4321")
                .build();

        session = new MockHttpSession();
        session.setAttribute(LOGIN_SESSION_KEY,3L);

        Optional<User> user = Optional.of(User.builder()
                        .email("test@gmail.com")
                        .pw("12341234")
                        .nickName("JyuKa")
                        .build());
        given(userRepository.findById(any()))
                .willReturn(user);

        String hashNewPw = BCrypt.hashpw(changePwDto.getNewPw(),BCrypt.gensalt());
        given(encryptor.encrypt(changePwDto.getNewPw()))
                .willReturn(hashNewPw);
        given(encryptor.isMatch(any(),any()))
                .willReturn(true);

        //When
        userService.modifyPassword(changePwDto,session);

        //Then
        then(user.get().getPw().equals(changePwDto.getNowPw())).isFalse();
        verify(userRepository).findById(any());
        verify(encryptor).encrypt(changePwDto.getNewPw());
        verify(encryptor).isMatch(any(),any());
    }

    @DisplayName("현재 비밀번호가 불일치 할때 PASSWORD_NOT_MATCH 응답")
    @Test
    public void givenNotMatchPw_whenChangePw_thenReturnPasswordNotMatch(){
        //Given
        ModifyPwDto.ChangePwDto changePwDto = ModifyPwDto.ChangePwDto.builder()
                .nowPw("1234").newPw("4321").confPw("4321")
                .build();

        session = new MockHttpSession();
        session.setAttribute(LOGIN_SESSION_KEY,3L);

        given(encryptor.isMatch(any(),any()))
                .willReturn(false);

        Optional<User> user = Optional.of(User.builder()
                .email("test@gmail.com")
                .pw("12341234")
                .nickName("JyuKa")
                .build());
        given(userRepository.findById(any()))
                .willReturn(user);

        //When
        Throwable thrown = catchThrowable(()->userService.modifyPassword(changePwDto,session));

        //Then
        then(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.PASSWORD_NOT_MATCH.getMessage());
        verify(encryptor).isMatch(any(),any());
        verify(userRepository).findById(any());
    }

    @DisplayName("새 비밀번호, 새 비밀먼호 확인이 불일치 할 때")
    @Test
    public void givenDifferentPws_whenChangePw_thenReturnDifferentNewPw(){
        //Given
        ModifyPwDto.ChangePwDto changePwDto = ModifyPwDto.ChangePwDto.builder()
                .nowPw("1234").newPw("4321").confPw("431")
                .build();

        session = new MockHttpSession();
        session.setAttribute(LOGIN_SESSION_KEY,3L);

        Optional<User> user = Optional.of(User.builder()
                .email("test@gmail.com")
                .pw("1234")
                .nickName("JyuKa")
                .build());
        given(userRepository.findById(any()))
                .willReturn(user);

        given(encryptor.isMatch(any(),any()))
                .willReturn(true);

        //When
        Throwable thrown = catchThrowable(()->userService.modifyPassword(changePwDto,session));

        //Then
        then(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DIFFERENT_NEW_PW.getMessage());
        verify(encryptor).isMatch(any(),any());
        verify(userRepository).findById(any());
    }




    public User createUser(SignUpUserDto.signUpRequest request){

        return User.builder()
                .email(request.getEmail())
                .pw(request.getPw())
                .nickName(request.getNickName())
                .build();
    }

    public SignUpUserDto.signUpRequest createSignUpRequest(){


        return SignUpUserDto.signUpRequest
                .builder()
                .email("test@test.com")
                .pw("1234")
                .nickName("user1")
                .build();
    }


}