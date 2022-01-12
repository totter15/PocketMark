package com.example.pocketmark.controller.api;

import com.example.pocketmark.dto.*;
import com.example.pocketmark.dto.common.ApiDataResponse;


import com.example.pocketmark.security.provider.UserPrincipal;
import com.example.pocketmark.service.LoginService;
import com.example.pocketmark.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserApiController {

    private final LoginService loginService;
    private final UserService userService;

    private Long getUserId(){
        UserPrincipal userPrincipal = (UserPrincipal)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return Long.parseLong(userPrincipal.getUsername());
    }


    @PostMapping("/sign-up")
    public ApiDataResponse<SignUpUserDto.signUpResponse> signUp(
            @Valid @RequestBody SignUpUserDto.signUpRequest request
    ){

        loginService.signUp(SignUpUserDto.SignUpDto.fromSignUpRequest(request));
        return ApiDataResponse.empty();

    }

    @GetMapping("/myPage")
    public ApiDataResponse<MyPageDto> myPage(){
        return ApiDataResponse.of(
                MyPageDto.fromUser(
                        userService.selectUserByUserId(getUserId())
                )
        );

    }

    @PutMapping("/changePassword")
    public ApiDataResponse<ModifyPwDto.ChangePwResponse> changePassword(
            @Valid @RequestBody ModifyPwDto.ChangePwRequest request
    ){

        userService.modifyPassword(ModifyPwDto.ChangePwDto.fromChangePwRequest(request),getUserId());
        return ApiDataResponse.empty();
    }


    @PutMapping("/changeNickName")
    public ApiDataResponse<ModifyNickNameDto.ChangeNickNameResponse> changeNickName(
            @Valid @RequestBody ModifyNickNameDto.ChangeNickNameRequest request
    ){
        userService.modifyNickName(ModifyNickNameDto.ChangeNickNameDto.fromChangeNickNameRequest(request),getUserId());
        return ApiDataResponse.empty();
    }

    @PutMapping("/userLeave")
    public ApiDataResponse<LeaveUser.LeaveUserResponse> leaveUser(
            @Valid @RequestBody LeaveUser.LeaveUserRequest request
    ){
        userService.deleteUser(LeaveUser.LeaveUserDto.fromLeaveUserRequest(request),getUserId());
        return ApiDataResponse.empty();
    }

    @PostMapping("/email-check")
    public ApiDataResponse<EmailCheck.EmailCheckRes> emailCheck(
            @Valid @RequestBody EmailCheck.EmailCheckReq request
    ){
       boolean result = userService
               .checkAvailableEmail(EmailCheck.EmailCheckDto.fromEmailCheckRequest(request));


        return ApiDataResponse.of(
                EmailCheck.EmailCheckRes
                        .builder()
                        .available(result)
                        .build());
    }

    @PostMapping("/alias-check")
    public ApiDataResponse<NickNameCheck.NickNameCheckRes> nickNameCheck(
            @Valid @RequestBody NickNameCheck.NickNameCheckReq request
    ){
        boolean result = userService
                .checkAvailableNickName(NickNameCheck.NickNameCheckDto.fromNickNameCheckRequest(request));


        return ApiDataResponse.of(
                NickNameCheck.NickNameCheckRes
                        .builder()
                        .available(result)
                        .build()
        );
    }



}
