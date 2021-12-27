package com.example.pocketmark.controller.api;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.dto.UserDto;
import com.example.pocketmark.service.LoginService;
import com.example.pocketmark.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("API 컨트롤러 - User")
@WebMvcTest(UserApiController.class)
class UserApiControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    @MockBean
    private LoginService loginService;

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
        UserDto.signUpRequest request = UserDto.signUpRequest.builder()
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
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
    }

}