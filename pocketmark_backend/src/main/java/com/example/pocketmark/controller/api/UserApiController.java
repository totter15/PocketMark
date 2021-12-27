package com.example.pocketmark.controller.api;

import com.example.pocketmark.dto.common.ApiDataResponse;
import com.example.pocketmark.exception.GeneralException;

import java.sql.SQLException;

import com.example.pocketmark.dto.UserDto;
import com.example.pocketmark.dto.UserDto.signUpRequest;
import com.example.pocketmark.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserApiController {

    private final LoginService loginService;

    @PostMapping("/sign-up")
    public ApiDataResponse<UserDto.signUpResponse> signUp(@RequestBody UserDto.signUpRequest request){
        try{
            loginService.signUp(request);
            return ApiDataResponse.of(new UserDto.signUpResponse(false,"Access Token"));
        }catch(Exception e){
            // throw new GeneralException(); //uhm ...?
            return ApiDataResponse.of(new UserDto.signUpResponse(true,"failed"));
        }

    }

}
