package com.example.pocketmark.controller.api;

import com.example.pocketmark.dto.*;
import com.example.pocketmark.dto.LoginDto.LoginReq;
import com.example.pocketmark.dto.common.ApiDataResponse;
import com.example.pocketmark.dto.common.ApiDataResponse.GeneralResponse;
import com.example.pocketmark.security.authentication.UserAuthentication;
import com.example.pocketmark.security.provider.JwtProvider;
import com.example.pocketmark.service.LoginService;
import com.example.pocketmark.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserApiController {
    public final static String LOGIN_SESSION_KEY = "USER_ID";
    private final LoginService loginService;
    private final UserService userService;

    @PostMapping("/sign-up")
    public ApiDataResponse<SignUpUserDto.signUpResponse> signUp(
            @Valid @RequestBody SignUpUserDto.signUpRequest request,
            HttpSession httpSession
    ){

        loginService.signUp(SignUpUserDto.SignUpDto.fromSignUpRequest(request),httpSession);
        return ApiDataResponse.of(new SignUpUserDto.signUpResponse(false,"Access Token"));

    }

    @GetMapping("/myPage")
    public ApiDataResponse<MyPageDto> myPage(HttpSession session){
        return ApiDataResponse.of(MyPageDto.fromUser(userService.selectUser(session)));

    }

    @PutMapping("/changePassword")
    public ApiDataResponse<ModifyPwDto.ChangePwResponse> changePassword(
            @Valid @RequestBody ModifyPwDto.ChangePwRequest request,
            HttpSession httpSession
    ){

        userService.modifyPassword(ModifyPwDto.ChangePwDto.fromChangePwRequest(request),httpSession);
        return ApiDataResponse.empty();
    }


    @PutMapping("/changeNickName")
    public ApiDataResponse<ModifyNickNameDto.ChangeNickNameResponse> changeNickName(
            @Valid @RequestBody ModifyNickNameDto.ChangeNickNameRequest request,
            HttpSession httpSession
    ){
        userService.modifyNickName(ModifyNickNameDto.ChangeNickNameDto.fromChangeNickNameRequest(request),httpSession);
        return ApiDataResponse.empty();
    }

    @PutMapping("/userLeave")
    public ApiDataResponse<LeaveUser.LeaveUserResponse> leaveUser(
            @Valid @RequestBody LeaveUser.LeaveUserRequest request,
            HttpSession httpSession
    ){
        userService.deleteUser(LeaveUser.LeaveUserDto.fromLeaveUserRequest(request),httpSession);
        return ApiDataResponse.empty();
    }

    @PostMapping("/login")
    public ApiDataResponse<GeneralResponse> login(
        @RequestBody LoginReq req,
        HttpServletResponse res
    ){
        System.out.println("삐빅");
        Long authId = loginService.login(req);
        if(authId!=null){
            //give jwt token
            UserAuthentication authentication = new UserAuthentication(String.valueOf(authId), null, null); 
            String token = JwtProvider.make(authentication,String.valueOf(authId));
            res.setHeader(HttpHeaders.AUTHORIZATION, token);
            return ApiDataResponse.success();
        }

        return ApiDataResponse.failed();
    }

    
    


}
