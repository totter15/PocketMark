package com.example.pocketmark.dto;

import lombok.*;

public class SendAuthenticationEmail {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class SendAuthenticationEmailDto{

        private String email;

        public static SendAuthenticationEmailDto fromSendAuthenticationEmailReq(SendAuthenticationEmailReq request){
            return SendAuthenticationEmailDto.builder()
                    .email(request.getEmail())
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
    public static class SendAuthenticationEmailReq{
        private String email;
    }

//
//    @Getter
//    @Setter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Builder
//    @ToString
//    public static class SendAuthenticationEmailRes{
//
//        private boolean success;
//
//    }



}
