package com.example.pocketmark.dto;

import lombok.*;

public class AuthenticationEmail {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class AuthenticationEmailDto{

        private String email;
        private String code;

        public static AuthenticationEmailDto fromSendAuthenticationEmailReq(AuthenticationEmailReq request){
            return AuthenticationEmailDto.builder()
                    .email(request.getEmail())
                    .code(request.getCode())
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
    public static class AuthenticationEmailReq{
        private String email;
        private String code;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class AuthenticationEmailRes{

        private boolean success;

    }
}
