package com.example.pocketmark.controller.api;

import com.example.pocketmark.dto.*;
import com.example.pocketmark.dto.LoginDto.LoginReq;
import com.example.pocketmark.dto.common.ApiDataResponse;
import com.example.pocketmark.service.LoginService;
import com.example.pocketmark.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserApiController {

    private final LoginService loginService;
    private final UserService userService;

    @PostMapping("/sign-up")
    public ApiDataResponse<SignUpUserDto.signUpResponse> signUp(
            @Valid @RequestBody SignUpUserDto.signUpRequest request
    ){

        loginService.signUp(SignUpUserDto.SignUpDto.fromSignUpRequest(request));
        return ApiDataResponse.empty();

    }

    @GetMapping("/myPage")
    public ApiDataResponse<MyPageDto> myPage(@AuthenticationPrincipal String email){
        return ApiDataResponse.of(MyPageDto.fromUser(userService.selectUserByToken(email)));

    }

    @PutMapping("/changePassword")
    public ApiDataResponse<ModifyPwDto.ChangePwResponse> changePassword(
            @Valid @RequestBody ModifyPwDto.ChangePwRequest request,
            @AuthenticationPrincipal String email
    ){

        userService.modifyPassword(ModifyPwDto.ChangePwDto.fromChangePwRequest(request),email);
        return ApiDataResponse.empty();
    }


    @PutMapping("/changeNickName")
    public ApiDataResponse<ModifyNickNameDto.ChangeNickNameResponse> changeNickName(
            @Valid @RequestBody ModifyNickNameDto.ChangeNickNameRequest request,
            @AuthenticationPrincipal String email
    ){
        userService.modifyNickName(ModifyNickNameDto.ChangeNickNameDto.fromChangeNickNameRequest(request),email);
        return ApiDataResponse.empty();
    }

    @PutMapping("/userLeave")
    public ApiDataResponse<LeaveUser.LeaveUserResponse> leaveUser(
            @Valid @RequestBody LeaveUser.LeaveUserRequest request,
            @AuthenticationPrincipal String email
    ){
        userService.deleteUser(LeaveUser.LeaveUserDto.fromLeaveUserRequest(request),email);
        return ApiDataResponse.empty();
    }

    @PostMapping("/login")
    public ApiDataResponse<LoginDto.LoginRes> login(
        @RequestBody LoginReq req,
        HttpServletResponse res
    ){

        return ApiDataResponse.of(LoginDto.LoginRes.builder()
                .tokenBox(loginService.login(req))
                .build());
    }

    
    


}
