package com.example.pocketmark.dto;

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
    }

    public static class auth{
        
    }

    
}
