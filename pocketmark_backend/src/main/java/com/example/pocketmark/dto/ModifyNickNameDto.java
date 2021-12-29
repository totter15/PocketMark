package com.example.pocketmark.dto;

import lombok.*;

public class ModifyNickNameDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class ChangeNickNameDto{
        private String newNickName;


        public static ChangeNickNameDto fromChangeNickNameRequest(ChangeNickNameRequest request){
            return ChangeNickNameDto.builder()
                    .newNickName(request.getNewNickName())
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
    public static class ChangeNickNameRequest{
        private String newNickName;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class ChangeNickNameResponse{
        private boolean isDuplicated;
        private String jwt;
    }
}
