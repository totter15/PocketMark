package com.example.pocketmark.controller.api;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.LeaveUser;
import com.example.pocketmark.dto.ModifyNickNameDto;
import com.example.pocketmark.dto.ModifyPwDto;
import com.example.pocketmark.dto.SignUpUserDto;
import com.example.pocketmark.service.LoginService;
import com.example.pocketmark.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;


import static com.example.pocketmark.controller.api.UserApiController.LOGIN_SESSION_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("API 컨트롤러 - User")
@WebMvcTest(UserApiController.class)
class UserApiControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;
    MockHttpSession session;

    @MockBean
    private LoginService loginService;

    @MockBean
    private UserService userService;

    public UserApiControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper)
    {
        this.mvc = mvc;
        this.mapper = mapper;
    }

    @DisplayName("[API][POST] 일반 유저 가입 - 정상 입력하면 회원정보를 추가")
    @Test
    public void givenSignUpRequest_whenSignUp_thenReturnApiDataResponseEmpty() throws Exception {
        //Given
        SignUpUserDto.signUpRequest request = SignUpUserDto.signUpRequest.builder()
                .email("test@gmail.com")
                .nickName("JyuKa")
                .pw("12345787845")
                .build()
                ;

        //When
        //Then
        mvc.perform(
                post("/api/v1/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()))
                .andExpect(jsonPath("$.data.duplicated").value(false))
                .andExpect(jsonPath("$.data.jwt").value("Access Token"))

        ;
    }

    @DisplayName("[API][GET] 내정보 조회 - 로그인 상태에서 내 정보 조회")
    @Test
    public void givenUserIdBySession_whenSelectUser_thenReturnMyPageDto() throws Exception {
        //Given
        given(userService.selectUser(any()))
                .willReturn(User.builder()
                        .email("test@gmail.com")
                        .pw("12341234")
                        .nickName("JyuKa")
                        .build()
                );

        //When
        //Then
        mvc.perform(
                get("/api/v1/myPage")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()))
                .andExpect(jsonPath("$.data.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.data.nickName").value("JyuKa"))

        ;
    }

    @DisplayName("[API][PUT] 회원정보 - 비밀번호 변경")
    @Test
    public void givenChangePwRequest_whenChangePw_thenReturnChangePwResponse() throws Exception {
        //Given
        ModifyPwDto.ChangePwRequest request = ModifyPwDto.ChangePwRequest
                .builder()
                .nowPw("1234").newPw("4321").confPw("4321")
                .build();

        session = new MockHttpSession();
        session.setAttribute(LOGIN_SESSION_KEY,1L);



        //When
        //Then
        mvc.perform(
                        put("/api/v1/changePassword")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()))
        ;
    }


    @DisplayName("[API][PUT] 회원정보 - 닉네임 변경")
    @Test
    public void givenChangeNickNameRequest_whenChangeNickName_thenReturnChangeNickNameResponse() throws Exception {
        //Given
        ModifyNickNameDto.ChangeNickNameRequest request =
                ModifyNickNameDto.ChangeNickNameRequest.builder()
                        .newNickName("JyuKa1")
                        .build();
        session = new MockHttpSession();
        session.setAttribute(LOGIN_SESSION_KEY,1L);



        //When
        //Then
        mvc.perform(
                        put("/api/v1/changeNickName")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()))
        ;
    }

    @DisplayName("[API][PUT] 회원정보 - 삭제")
    @Test
    public void givenLeaveUserRequest_whenDeleteUser_thenReturnLeaveUserResponse() throws Exception {
        //Given
        LeaveUser.LeaveUserRequest request = LeaveUser.LeaveUserRequest.builder()
                .leave(true)
                .build();
        session = new MockHttpSession();
        session.setAttribute(LOGIN_SESSION_KEY,1L);



        //When
        //Then
        mvc.perform(
                        put("/api/v1/userLeave")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()))
        ;
    }

}