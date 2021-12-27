package com.example.pocketmark.dto;

import lombok.*;

public class UserDto {

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
