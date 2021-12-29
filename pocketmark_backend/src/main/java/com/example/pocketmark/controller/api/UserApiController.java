package com.example.pocketmark.controller.api;

import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.ModifyNickNameDto;
import com.example.pocketmark.dto.ModifyPwDto;
import com.example.pocketmark.dto.MyPageDto;
import com.example.pocketmark.dto.common.ApiDataResponse;

import com.example.pocketmark.dto.SignUpUserDto;
import com.example.pocketmark.service.LoginService;
import com.example.pocketmark.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserApiController {
    public final static String LOGIN_SESSION_KEY = "USER_ID";
    private final LoginService loginService;
    private final UserService userService;

    @PostMapping("/sign-up")
    public ApiDataResponse<SignUpUserDto.signUpResponse> signUp(
            @RequestBody SignUpUserDto.signUpRequest request,
            HttpSession httpSession
    ){

        loginService.signUp(SignUpUserDto.SignUpDto.fromSignUpRequest(request),httpSession);
        return ApiDataResponse.of(new SignUpUserDto.signUpResponse(false,"Access Token"));

    }

    @GetMapping("/myPage")
    public ApiDataResponse<MyPageDto> myPage(HttpSession session){
        return ApiDataResponse.of(MyPageDto.fromUser(userService.selectUser(session)));

    }

    @PostMapping("/changePassword")
    public ApiDataResponse<ModifyPwDto.ChangePwResponse> changePassword(
            @RequestBody ModifyPwDto.ChangePwRequest request,
            HttpSession httpSession
    ){

        userService.modifyPassword(ModifyPwDto.ChangePwDto.fromChangePwRequest(request),httpSession);
        return ApiDataResponse.empty();
    }


    @PostMapping("/changeNickName")
    public ApiDataResponse<ModifyNickNameDto.ChangeNickNameResponse> changeNickName(
            @RequestBody ModifyNickNameDto.ChangeNickNameRequest request,
            HttpSession httpSession
    ){
        userService.modifyNickName(ModifyNickNameDto.ChangeNickNameDto.fromChangeNickNameRequest(request),httpSession);
        return ApiDataResponse.empty();
    }

}
