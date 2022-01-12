package com.example.pocketmark.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class NickNameCheck {


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class NickNameCheckDto{
        private String nickName;

        public static NickNameCheckDto fromNickNameCheckRequest(NickNameCheckReq request){
            return NickNameCheckDto.builder()
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
    public static class NickNameCheckReq{
        @Size(min = 2, max = 12)
        private String nickName;

    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class NickNameCheckRes{
        boolean available;
    }
}
