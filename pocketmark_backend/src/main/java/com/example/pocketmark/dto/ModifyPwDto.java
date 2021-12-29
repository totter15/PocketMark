package com.example.pocketmark.dto;

import lombok.*;

import javax.validation.constraints.Size;

public class ModifyPwDto {


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class ChangePwDto{
        private String nowPw;
        private String newPw;
        private String confPw;

        public static ChangePwDto fromChangePwRequest(ChangePwRequest request){
            return ChangePwDto.builder()
                    .nowPw(request.getNowPw())
                    .newPw(request.getNewPw())
                    .confPw(request.getConfPw())
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
    public static class ChangePwRequest{
        @Size(min=10)
        private String nowPw;

        @Size(min=10)
        private String newPw;

        @Size(min=10)
        private String confPw;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class ChangePwResponse{
        private boolean isDuplicated;
        private String jwt;
    }
}
