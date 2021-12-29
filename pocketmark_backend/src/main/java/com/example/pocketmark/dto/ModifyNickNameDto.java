package com.example.pocketmark.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

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

        @Size(min = 2, max = 12)
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
