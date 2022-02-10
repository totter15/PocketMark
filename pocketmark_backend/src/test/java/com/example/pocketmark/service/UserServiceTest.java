// package com.example.pocketmark.service;

// import com.example.pocketmark.constant.ErrorCode;
// import com.example.pocketmark.domain.Authority;
// import com.example.pocketmark.domain.User;
// import com.example.pocketmark.dto.*;
// import com.example.pocketmark.exception.GeneralException;
// import com.example.pocketmark.repository.UserRepository;
// import com.example.pocketmark.security.provider.UserPrincipal;
// import com.example.pocketmark.util.Encryptor;
// import org.junit.jupiter.api.Disabled;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mindrot.jbcrypt.BCrypt;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.dao.DataIntegrityViolationException;

// import java.sql.SQLException;
// import java.util.Optional;
// import java.util.Set;


// import static org.assertj.core.api.Assertions.catchThrowable;
// import static org.assertj.core.api.BDDAssertions.then;
// import static org.hamcrest.MatcherAssert.assertThat;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.BDDMockito.given;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.assertj.core.api.Assertions.assertThat;


// @DisplayName("UserService 테스트")
// @ExtendWith(MockitoExtension.class)
// class UserServiceTest {

//     @InjectMocks
//     private UserService userService;

//     @Mock
//     private Encryptor encryptor;

//     @Mock
//     private UserRepository userRepository;


//     @DisplayName("아무런 중복이 없는 조건을 입력하여 유저를 저장한다")
//     @Test
//     public void givenNotOverlapNickNameAndEmail_whenSaveUser_thenReturnUser() throws SQLException{
//         //Given
//         SignUpUserDto.signUpRequest request = createSignUpRequest();

//         given(userRepository.save(any()))
//                 .willReturn(createUser(request));

//         //When
//         User user = userService.create(SignUpUserDto.SignUpDto.fromSignUpRequest(request));


//         //Then
//         then(user.getEmail()).isEqualTo(request.getEmail());
//         then(user.getPw()).isEqualTo(request.getPw());
//         then(user.getNickName()).isEqualTo(request.getNickName());
//         verify(userRepository).save(any());
//     }

//     @Deprecated
//     @Disabled("DataIntegrityViolationException 추가로 인한 비활성화")
//     @DisplayName("중복된 닉네임으로 가입을 시도할떄 오류를 발생시킨다.")
//     @Test
//     public void givenOverlapNickName_whenSaveUser_thenReturnException(){
//         //Given
//         GeneralException e = new GeneralException(ErrorCode.NICKNAME_EXIST);
//         SignUpUserDto.signUpRequest request = createSignUpRequest();
//         given(userRepository.findByNickName(any()))
//                 .willThrow(e);

//         //When
//         Throwable thrown = catchThrowable(()->userService.create(SignUpUserDto.SignUpDto.fromSignUpRequest(request)));

//         //Then
//         then(thrown)
//                 .isInstanceOf(GeneralException.class)
//                 .hasMessageContaining(ErrorCode.NICKNAME_EXIST.getMessage());
//         verify(userRepository).findByNickName(any());
//     }

//     @DisplayName("중복된 이메일이나 닉네임을 저장하려하면 DataIntegrityViolationException 에러를 발생시킨다. - unique 제약조건 테스트")
//     @Test
//     public void givenOverlapEmailOrNickName_whenSaveUser_thenReturnException(){
//         //Given
//         DataIntegrityViolationException e = new DataIntegrityViolationException("...");
//         SignUpUserDto.signUpRequest request = createSignUpRequest();
//         SignUpUserDto.SignUpDto dto = SignUpUserDto.SignUpDto.fromSignUpRequest(request);
//         given(userRepository.save(any()))
//                 .willThrow(e);


//         //When
//         Throwable thrown = catchThrowable(()->userService.create(dto));

//         //Then
//         then(thrown)
//                 .isInstanceOf(DataIntegrityViolationException.class);
//         verify(userRepository).save(any());

//     }

//     @Deprecated
//     @Disabled("DataIntegrityViolationException 추가로 인한 비활성화")
//     @DisplayName("중복뒨 이메일로 가입을 시도할때 오류를 발생시킨다.")
//     @Test
//     public void givenOverlapEmail_whenSaveUser_thenReturnException(){
//         //Given
//         GeneralException e = new GeneralException(ErrorCode.EMAIL_EXIST);
//         SignUpUserDto.signUpRequest request = createSignUpRequest();
//         given(userRepository.findByEmail(any()))
//                 .willThrow(e);

//         //When
//         Throwable thrown = catchThrowable(()->userService.create(SignUpUserDto.SignUpDto.fromSignUpRequest(request)));

//         //Then
//         then(thrown)
//                 .isInstanceOf(GeneralException.class)
//                 .hasMessageContaining(ErrorCode.EMAIL_EXIST.getMessage());
//         verify(userRepository).findByEmail(any());

//     }

//     @DisplayName("userId를 받아 유저정보를 조회한다.")
//     @Test
//     public void givenUserId_whenSelectUser_thenReturnUser(){
//         //Given
//         Long userId = 1L;
//         given(userRepository.findById(any()))
//                 .willReturn(
//                         Optional.of(User.builder()
//                                 .email("test@gmail.com")
//                                 .pw("12341234")
//                                 .nickName("JyuKa")
//                                 .build()
//                         )
//                 );

//         //When
//         User user = userService.selectUserByUserId(userId);

//         //Then
//         then(user.getEmail()).isEqualTo("test@gmail.com");
//         then(user.getNickName()).isEqualTo("JyuKa");
//         then(user.getPw()).isEqualTo("12341234");
//         verify(userRepository).findById(any());
//     }

//     @DisplayName("존재하지 않는 userId 를 받아 Exception 을 발생시킨다.")
//     @Test
//     public void givenNotExistUserId_whenSelectUser_thenReturnUnAuthorizeException(){
//         //Given
//         Long userId = 1L;
//         given(userRepository.findById(any()))
//                 .willReturn(Optional.empty());

//         //When
//         Throwable thrown = catchThrowable(()->userService.selectUserByUserId(userId));

//         //Then
//         then(thrown)
//                 .isInstanceOf(GeneralException.class)
//                 .hasMessageContaining(ErrorCode.ENTITY_NOT_EXIST.getMessage());
//     }


//     @DisplayName("정상적인 비밀번호 변경")
//     @Test
//     public void givenChangePwDto_whenChangePw_thenReturnChangePw(){
//         //Given
//         ModifyPwDto.ChangePwDto changePwDto = ModifyPwDto.ChangePwDto.builder()
//                 .nowPw("12341234").newPw("4321").confPw("4321")
//                 .build();
//         Long userId = 1L;


//         Optional<User> user = Optional.of(User.builder()
//                         .email("test@gmail.com")
//                         .pw("12341234")
//                         .nickName("JyuKa")
//                         .build());
//         given(userRepository.findById(any()))
//                 .willReturn(user);

//         String hashNewPw = BCrypt.hashpw(changePwDto.getNewPw(),BCrypt.gensalt());
//         given(encryptor.encrypt(changePwDto.getNewPw()))
//                 .willReturn(hashNewPw);

//         given(encryptor.isMatch(eq(changePwDto.getNowPw()),any()))
//                 .willReturn(true);

//         given(encryptor.isMatch(eq(changePwDto.getNewPw()),any()))
//                 .willReturn(false);

//         //When
//         userService.modifyPassword(changePwDto,userId);

//         //Then
//         then(user.get().getPw().equals(changePwDto.getNowPw())).isFalse();
//         verify(userRepository).findById(any());
//         verify(encryptor).encrypt(changePwDto.getNewPw());
//         verify(encryptor).isMatch(eq(changePwDto.getNowPw()),any());
//         verify(encryptor).isMatch(eq(changePwDto.getNewPw()),any());
//     }

//     @DisplayName("현재 비밀번호가 불일치 할때 PASSWORD_NOT_MATCH 응답")
//     @Test
//     public void givenNotMatchPw_whenChangePw_thenReturnPasswordNotMatch(){
//         //Given
//         ModifyPwDto.ChangePwDto changePwDto = ModifyPwDto.ChangePwDto.builder()
//                 .nowPw("1234").newPw("4321").confPw("4321")
//                 .build();
//         Long userId = 1L;


//         given(encryptor.isMatch(any(),any()))
//                 .willReturn(false);

//         Optional<User> user = Optional.of(User.builder()
//                 .email("test@gmail.com")
//                 .pw("12341234")
//                 .nickName("JyuKa")
//                 .build());
//         given(userRepository.findById(any()))
//                 .willReturn(user);

//         //When
//         Throwable thrown = catchThrowable(()->userService.modifyPassword(changePwDto,userId));

//         //Then
//         then(thrown)
//                 .isInstanceOf(GeneralException.class)
//                 .hasMessageContaining(ErrorCode.PASSWORD_NOT_MATCH.getMessage());
//         verify(encryptor).isMatch(any(),any());
//         verify(userRepository).findById(any());
//     }

//     @DisplayName("새 비밀번호, 새 비밀먼호 확인이 불일치 할 때")
//     @Test
//     public void givenDifferentPws_whenChangePw_thenReturnDifferentNewPw(){
//         //Given
//         ModifyPwDto.ChangePwDto changePwDto = ModifyPwDto.ChangePwDto.builder()
//                 .nowPw("1234").newPw("4321").confPw("431")
//                 .build();
//         Long userId = 1L;


//         Optional<User> user = Optional.of(User.builder()
//                 .email("test@gmail.com")
//                 .pw("1234")
//                 .nickName("JyuKa")
//                 .build());
//         given(userRepository.findById(any()))
//                 .willReturn(user);

//         given(encryptor.isMatch(eq(changePwDto.getNowPw()),any()))
//                 .willReturn(true);

//         given(encryptor.isMatch(eq(changePwDto.getNewPw()),any()))
//                 .willReturn(false);

//         //When
//         Throwable thrown = catchThrowable(()->userService.modifyPassword(changePwDto,userId));

//         //Then
//         then(thrown)
//                 .isInstanceOf(GeneralException.class)
//                 .hasMessageContaining(ErrorCode.DIFFERENT_NEW_PW.getMessage());
//         verify(encryptor).isMatch(eq(changePwDto.getNowPw()),any());
//         verify(encryptor).isMatch(eq(changePwDto.getNewPw()),any());
//         verify(userRepository).findById(any());
//     }


//     @DisplayName("현재 비밀번호와 새 비밀번호가 일치할 경우")
//     @Test
//     public void givenNowPwEqualsNewPw_whenChangePw_thenReturnPassWordMatchException(){
//         //Given
//         ModifyPwDto.ChangePwDto changePwDto = ModifyPwDto.ChangePwDto.builder()
//                 .nowPw("1234").newPw("1234").confPw("1234")
//                 .build();
//         Long userId = 1L;


//         Optional<User> user = Optional.of(User.builder()
//                 .email("test@gmail.com")
//                 .pw("1234")
//                 .nickName("JyuKa")
//                 .build());
//         given(userRepository.findById(any()))
//                 .willReturn(user);

//         given(encryptor.isMatch(eq(changePwDto.getNowPw()),any()))
//                 .willReturn(true);


//         //When
//         Throwable thrown = catchThrowable(()->userService.modifyPassword(changePwDto,userId));

//         //Then
//         then(thrown)
//                 .isInstanceOf(GeneralException.class)
//                 .hasMessageContaining(ErrorCode.PASSWORD_MATCH.getMessage());
//         verify(encryptor,times(2)).isMatch(eq(changePwDto.getNowPw()),any());
//         verify(userRepository).findById(any());
//     }

//     @DisplayName("정상적인 닉네임 변경")
//     @Test
//     public void givenChangeNickNameDto_whenChangeNickName_thenReturnChangeNickName(){
//         //Given
//         ModifyNickNameDto.ChangeNickNameDto changeNickNameDto =
//                 ModifyNickNameDto.ChangeNickNameDto.builder()
//                         .newNickName("JyuKa1")
//                         .build();

//         Long userId = 1L;

//         Optional<User> user = Optional.of(User.builder()
//                 .email("test@gmail.com")
//                 .pw("12341234")
//                 .nickName("JyuKa")
//                 .build());

//         given(userRepository.findById(any()))
//                 .willReturn(user);

//         given(userRepository.existsByNickName(anyString()))
//                 .willReturn(false);


//         //When
//         userService.modifyNickName(changeNickNameDto,userId);

//         //Then
//         then(user.get().getNickName()).isEqualTo(changeNickNameDto.getNewNickName());
//         verify(userRepository).findById(any());
//         verify(userRepository).existsByNickName(anyString());
//     }

//     @DisplayName("중복된 닉네임으로 변경을 시도할 떄 예외를 출력한다")
//     @Test
//     public void givenExistNickName_whenChangeNickName_thenReturnNickNameExistException(){
//         //Given
//         ModifyNickNameDto.ChangeNickNameDto changeNickNameDto =
//                 ModifyNickNameDto.ChangeNickNameDto.builder()
//                         .newNickName("JyuKa1")
//                         .build();

//         Long userId = 1L;

//         Optional<User> user = Optional.of(User.builder()
//                 .email("test@gmail.com")
//                 .pw("12341234")
//                 .nickName("JyuKa")
//                 .build());

//         given(userRepository.findById(any()))
//                 .willReturn(user);

//         given(userRepository.existsByNickName(anyString()))
//                 .willReturn(true);




//         //When
//         Throwable thrown = catchThrowable(()->userService.modifyNickName(changeNickNameDto,userId));

//         //Then
//         then(thrown)
//                 .isInstanceOf(GeneralException.class)
//                 .hasMessageContaining(ErrorCode.NICKNAME_EXIST.getMessage());
//         verify(userRepository).findById(any());
//         verify(userRepository).existsByNickName(anyString());
//     }

//     @DisplayName("정상적인 닉네임 변경")
//     @Test
//     public void givenLeaveUserDto_whenDeleteUser_thenReturnDeleteUser(){
//         //Given
//         LeaveUser.LeaveUserDto dto = LeaveUser.LeaveUserDto.builder()
//                 .leave(true)
//                 .build();

//         Long userId = 1L;

//         Optional<User> user = Optional.of(User.builder()
//                 .email("test@gmail.com")
//                 .pw("12341234")
//                 .nickName("JyuKa")
//                 .build());

//         given(userRepository.findById(any()))
//                 .willReturn(user);



//         //When
//         userService.deleteUser(dto,userId);

//         //Then
//         then(user.get().isDeleted()).isEqualTo(dto.isLeave());
//         verify(userRepository).findById(any());
//     }

//     @DisplayName("[Success] Security Filter : loadUserByUsername")
//     @Test
//     public void givenEmail_whenLoadUser_returnUser(){
//         //Given
//         Optional<User> user = createOptionalUser();
//         user.get().setId(1L);
//         user.get().setAuthorities(Set.of(Authority.USER_AUTHORITY));


//         given(userRepository.findByEmail(any()))
//                 .willReturn(user);

//         //When
//         UserPrincipal savedUser = (UserPrincipal) userService.loadUserByUsername(user.get().getEmail());

//         //Then
//         then(savedUser.getUsername()).isEqualTo("1");
//         then(savedUser.getAuthorities().size()).isEqualTo(1);
//         verify(userRepository).findByEmail(any());
//     }


//     @DisplayName("[Fail] Security Filter : loadUserByUsername")
//     @Test
//     public void givenEmail_whenLoadUser_returnException(){
//         //Given
//         given(userRepository.findByEmail(any()))
//                 .willThrow(new GeneralException(ErrorCode.ENTITY_NOT_EXIST));

//         //When
//         Throwable thrown = catchThrowable(()->userService.loadUserByUsername(any()));

//         //Then
//         then(thrown)
//                 .isInstanceOf(GeneralException.class)
//                 .hasMessageContaining(ErrorCode.ENTITY_NOT_EXIST.getMessage());
//         verify(userRepository).findByEmail(any());
//     }

//     @DisplayName("User 권한 추가 - 아무런 권한이 없는 경우")
//     @Test
//     public void givenAnyAuthority_whenAddFirstAuthority_thenAddSuccess(){
//         //Given
//         Optional<User> savedUser = createOptionalUser();
//         given(userRepository.findByEmail(any()))
//                 .willReturn(savedUser);
//         Authority authority = new Authority("ROLE_TEST");

//         //When
//         userService.addAuthority(savedUser.get().getEmail(), "ROLE_TEST");

//         //Then
//         then(savedUser.get().getAuthorities().size()).isEqualTo(1);
//         then(savedUser.get().getAuthorities().contains(authority)).isTrue();
//         verify(userRepository).findByEmail(any());

//     }

//     @DisplayName("User 권한 추가 - 기존권한이 있는 경우")
//     @Test
//     public void givenAnyAuthority_whenAddAuthority_thenAddSuccess(){
//         //Given
//         Optional<User> savedUser = createOptionalUser();
//         Authority authority = new Authority("ROLE_USER");
//         savedUser.get().setAuthorities(Set.of(authority));
//         given(userRepository.findByEmail(any()))
//                 .willReturn(savedUser);

//         //When
//         userService.addAuthority(savedUser.get().getEmail(), "ROLE_TEST");

//         //Then
//         then(savedUser.get().getAuthorities().size()).isEqualTo(2);
//         then(savedUser.get().getAuthorities().contains(authority)).isTrue();
//         then(savedUser.get().getAuthorities().contains(new Authority("ROLE_TEST"))).isTrue();
//         verify(userRepository).findByEmail(any());

//     }

//     @DisplayName("User 권한 추가 - 이미 존재하는 권한을 추가 하는 경우")
//     @Test
//     public void givenExistAuthority_whenAddAuthority_thenRoleExistException(){
//         //Given
//         Optional<User> savedUser = createOptionalUser();
//         Authority authority = new Authority("ROLE_USER");
//         savedUser.get().setAuthorities(Set.of(authority));
//         given(userRepository.findByEmail(any()))
//                 .willReturn(savedUser);

//         //When
//         Throwable thrown = catchThrowable(()->
//                 userService.addAuthority(savedUser.get().getEmail(), "ROLE_USER"));

//         //Then
//         then(thrown)
//                 .isInstanceOf(GeneralException.class)
//                 .hasMessageContaining(ErrorCode.ROLE_EXIST.getMessage());
//         verify(userRepository).findByEmail(any());
//     }


//     @DisplayName("User 권한 추가 - email 이 존재하지 않을 경우")
//     @Test
//     public void givenNotExistEmail_whenAddAuthority_thenEntityNotExistException(){
//         //Given
//         Optional<User> savedUser = Optional.empty();
//         given(userRepository.findByEmail(any()))
//                 .willReturn(savedUser);

//         //When
//         Throwable thrown = catchThrowable(()->
//                 userService.addAuthority("test@gmail.com", "ROLE_USER"));

//         //Then
//         then(thrown)
//                 .isInstanceOf(GeneralException.class)
//                 .hasMessageContaining(ErrorCode.ENTITY_NOT_EXIST.getMessage());
//         verify(userRepository).findByEmail(any());
//     }

//     @DisplayName("User 권한 삭제 - email 이 존재하지 않을 경우")
//     @Test
//     public void givenNotExistEmail_whenRemoveAuthority_thenEntityNotExistException(){
//         //Given
//         Optional<User> savedUser = Optional.empty();
//         given(userRepository.findByEmail(any()))
//                 .willReturn(savedUser);

//         //When
//         Throwable thrown = catchThrowable(()->
//                 userService.removeAuthority("test@gmail.com", "ROLE_USER"));

//         //Then
//         then(thrown)
//                 .isInstanceOf(GeneralException.class)
//                 .hasMessageContaining(ErrorCode.ENTITY_NOT_EXIST.getMessage());
//         verify(userRepository).findByEmail(any());
//     }

//     @DisplayName("User 권한 삭제 - 존재하는 권한을 삭제 하는 경우")
//     @Test
//     public void givenExistAuthority_whenRemoveAuthority_thenSuccessRemove(){
//         //Given
//         Optional<User> savedUser = createOptionalUser();
//         Authority userAuthority = new Authority("ROLE_USER");
//         Authority testAuthority = new Authority("ROLE_TEST");
//         savedUser.get().setAuthorities(Set.of(userAuthority,testAuthority));

//         given(userRepository.findByEmail(any()))
//                 .willReturn(savedUser);

//         //When
//         userService.removeAuthority(savedUser.get().getEmail(),"ROLE_TEST");

//         //Then
//         then(savedUser.get().getAuthorities().size()).isEqualTo(1);
//         then(savedUser.get().getAuthorities().contains(userAuthority)).isTrue();
//         then(savedUser.get().getAuthorities().contains(testAuthority)).isFalse();
//         verify(userRepository).findByEmail(any());
//     }

//     @DisplayName("User 권한 삭제 - 존재하지 않는 권한을 삭제 하는 경우")
//     @Test
//     public void givenNotExistAuthority_whenRemoveAuthority_thenRoleNotExistException(){
//         //Given
//         Optional<User> savedUser = createOptionalUser();
//         Authority userAuthority = new Authority("ROLE_USER");
//         savedUser.get().setAuthorities(Set.of(userAuthority));

//         given(userRepository.findByEmail(any()))
//                 .willReturn(savedUser);

//         //When
//         Throwable thrown = catchThrowable(()->
//                 userService.removeAuthority(savedUser.get().getEmail(), "ROLE_TEST"));

//         //Then
//         then(thrown)
//                 .isInstanceOf(GeneralException.class)
//                 .hasMessageContaining(ErrorCode.ROLE_NOT_EXIST.getMessage());
//         verify(userRepository).findByEmail(any());

//     }

//     @DisplayName("User 권한 삭제 - 아무런 권한이 없는 유저를 대상으로 권한 삭제를 시도")
//     @Test
//     public void givenNotExistAuthorityAndUserAuthorityEmpty_whenRemoveAuthority_thenRoleNotExistException(){
//         //Given
//         Optional<User> savedUser = createOptionalUser();

//         given(userRepository.findByEmail(any()))
//                 .willReturn(savedUser);

//         //When
//         Throwable thrown = catchThrowable(()->
//                 userService.removeAuthority(savedUser.get().getEmail(), "ROLE_TEST"));

//         //Then
//         then(thrown)
//                 .isInstanceOf(GeneralException.class)
//                 .hasMessageContaining(ErrorCode.ROLE_NOT_EXIST.getMessage());
//         verify(userRepository).findByEmail(any());

//     }


//     @DisplayName("옳바른 이메일과 비밀번호를 입력하였을떄 유저 정보를 리턴한다.")
//     @Test
//     public void givenLoginReq_whenSelectUserByReq_thenReturnOptionalUser(){
//         //Given
//         LoginDto.LoginReq req = createLoginRequest();
//         given(userRepository.findByEmail(any()))
//                 .willReturn(createOptionalUser());

//         //When
//         User user = userService.selectUserByLoginReq(req);

//         //Then
//         then(user.getEmail()).isEqualTo("test@gmail.com");
//         then(user.getPw()).isEqualTo("1234");
//         verify(userRepository).findByEmail(any());
//     }

//     @DisplayName("옳바른 이메일과 틀린 비밀번호를 입력하였을떄 EMAIL_OR_PASSWORD_NOT_MATCH 가 발생한다")
//     @Test
//     public void givenWrongPw_whenSelectUserByReq_thenEmailOrPasswordNotMatchException(){
//         //Given
//         LoginDto.LoginReq req = createLoginRequest();
//         given(userRepository.findByEmail(any()))
//                 .willReturn(Optional.empty());

//         //When
//         Throwable thrown = catchThrowable(()->
//                 userService.selectUserByLoginReq(req));

//         //Then
//         then(thrown)
//                 .isInstanceOf(GeneralException.class)
//                 .hasMessageContaining(ErrorCode.ENTITY_NOT_EXIST.getMessage());
//         verify(userRepository).findByEmail(any());
//     }


//     @DisplayName("이메일 중복검사 - 사용가능")
//     @Test
//     public void givenNotDuplicateEmail_whenCheckAvailableEmail_thenReturnTrue(){
//         //Given
//         EmailCheck.EmailCheckDto dto = EmailCheck.EmailCheckDto.builder()
//                 .email("test@gmail.com")
//                 .build();

//         given(userRepository.existsByEmail(any()))
//                 .willReturn(false);

//         //When
//         boolean result = userService.checkAvailableEmail(dto);

//         //Then
//         then(result).isTrue();
//         verify(userRepository).existsByEmail(any());
//     }

//     @DisplayName("이메일 중복검사 - 사용불가")
//     @Test
//     public void givenDuplicateEmail_whenCheckAvailableEmail_thenReturnFalse(){
//         //Given
//         EmailCheck.EmailCheckDto dto = EmailCheck.EmailCheckDto.builder()
//                 .email("test@gmail.com")
//                 .build();

//         given(userRepository.existsByEmail(any()))
//                 .willReturn(true);

//         //When
//         boolean result = userService.checkAvailableEmail(dto);

//         //Then
//         then(result).isFalse();
//         verify(userRepository).existsByEmail(any());
//     }


//     @DisplayName("닉네임 중복검사 - 사용가능")
//     @Test
//     public void givenNotDuplicateNickName_whenCheckAvailableNickName_thenReturnTrue(){
//         //Given
//         NickNameCheck.NickNameCheckDto dto = NickNameCheck.NickNameCheckDto.builder()
//                 .nickName("JyuKa")
//                 .build();

//         given(userRepository.existsByNickName(any()))
//                 .willReturn(false);

//         //When
//         boolean result = userService.checkAvailableNickName(dto);

//         //Then
//         then(result).isTrue();
//         verify(userRepository).existsByNickName(any());
//     }


//     @DisplayName("닉네임 중복검사 - 사용불가")
//     @Test
//     public void givenDuplicateNickName_whenCheckAvailableNickName_thenReturnFalse(){
//         //Given
//         NickNameCheck.NickNameCheckDto dto = NickNameCheck.NickNameCheckDto.builder()
//                 .nickName("JyuKa")
//                 .build();

//         given(userRepository.existsByNickName(any()))
//                 .willReturn(true);

//         //When
//         boolean result = userService.checkAvailableNickName(dto);

//         //Then
//         then(result).isFalse();
//         verify(userRepository).existsByNickName(any());
//     }



//     public Optional<User> createOptionalUser(){
//         return Optional.of(
//                 User.builder()
//                         .email("test@gmail.com")
//                         .nickName("JyuKa")
//                         .pw("1234")
//                         .build()
//         );
//     }

//     public User createUser(SignUpUserDto.signUpRequest request){

//         return User.builder()
//                 .email(request.getEmail())
//                 .pw(request.getPw())
//                 .nickName(request.getNickName())
//                 .build();
//     }

//     public SignUpUserDto.signUpRequest createSignUpRequest(){


//         return SignUpUserDto.signUpRequest
//                 .builder()
//                 .email("test@test.com")
//                 .pw("1234")
//                 .nickName("user1")
//                 .build();
//     }

//     public LoginDto.LoginReq createLoginRequest(){
//         return LoginDto.LoginReq.builder()
//                 .email("test@gmail.com")
//                 .pw("1234")
//                 .build();
//     }
// }