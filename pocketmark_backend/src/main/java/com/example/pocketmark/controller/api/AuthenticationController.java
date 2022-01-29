package com.example.pocketmark.controller.api;

import com.example.pocketmark.dto.LoginDto;
import com.example.pocketmark.dto.RefreshToken;
import com.example.pocketmark.dto.common.ApiDataResponse;
import com.example.pocketmark.security.provider.JwtUtil;
import com.example.pocketmark.security.provider.TokenBox;
import com.example.pocketmark.service.AuthenticationService;
import com.example.pocketmark.service.DataService;
import com.example.pocketmark.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final DataService dataService;

    @PostMapping("/login")
    public ApiDataResponse<LoginDto.LoginRes> login(
            @RequestBody LoginDto.LoginReq req
    ){
        TokenBox tokenBox = authenticationService.authenticate(req);
        
        return ApiDataResponse.of(LoginDto.LoginRes.builder()
                .tokenBox(tokenBox)
                .itemId(dataService.getLastItemId(JwtUtil.getUserId(tokenBox.getAccessToken())))
                .build());

    }

    @PostMapping("/refresh-token")
    public ApiDataResponse<RefreshToken.RefreshTokenRes> refreshJwtToken(
            @RequestBody RefreshToken.RefreshTokenReq req
    ){
        RefreshToken.RefreshTokenRes res = refreshTokenService.refreshAccessToken(
                RefreshToken.RefreshTokenDto.fromRefreshTokenReq(req)
        );

        return ApiDataResponse.of(res);
    }
}
