package com.example.pocketmark.dto;


import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


public class SignUpUserDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class SignUpDto{

        private String email;
        private String pw;
        private String nickName;

        public static SignUpDto fromSignUpRequest(signUpRequest request){
            return SignUpDto.builder()
                    .email(request.getEmail())
                    .pw(request.getPw())
                    .nickName(request.getNickName())
                    .build()
                    ;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class signUpRequest{

        @Email
        private String email;

        @Size(min=7)
        private String pw;

        @Size(min = 2, max = 12)
        private String nickName;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class signUpResponse{
        private boolean isDuplicated;
        private String jwt;
    }
}
