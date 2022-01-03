package com.example.pocketmark.dto;

import javax.validation.constraints.Pattern;

import lombok.*;

public class UserDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class SignUpDto{
        @Pattern(regexp = "[A-Za-z0-9+_.-]+@[a-zA-z]+[.]+[a-zA-z]{2,3}$/")
        private String email;
        @Pattern(regexp = "/^[A-Za-z0-9]{6,20}$/") // 숫자문자포함 6~20자리 비밀번호
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
        private String email;
        private String pw;
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
