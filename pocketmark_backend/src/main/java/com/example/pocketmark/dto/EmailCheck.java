package com.example.pocketmark.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class EmailCheck {


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class EmailCheckDto{
        private String email;

        public static EmailCheckDto fromEmailCheckRequest(EmailCheckReq request){
            return EmailCheckDto.builder()
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
    public static class EmailCheckReq{
        @Email
        private String email;

    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class EmailCheckRes{
        boolean available;
    }
}
