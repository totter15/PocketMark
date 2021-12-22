package com.example.pocketmark.controller.api;

import com.example.pocketmark.dto.common.ApiDataResponse;
import com.example.pocketmark.dto.UserDto;
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
        loginService.signUp(request);
        return ApiDataResponse.empty();

    }

}
