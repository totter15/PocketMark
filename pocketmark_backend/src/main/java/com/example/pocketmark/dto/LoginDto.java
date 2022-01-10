package com.example.pocketmark.dto;

import com.example.pocketmark.security.provider.TokenBox;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class LoginDto {


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginReq{
        private String email;
        private String pw;
        private String refreshToken;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginRes{
        private TokenBox tokenBox;
    }

    
}
