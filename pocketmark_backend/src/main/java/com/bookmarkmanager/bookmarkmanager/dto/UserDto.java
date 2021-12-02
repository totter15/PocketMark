package com.bookmarkmanager.bookmarkmanager.dto;

import com.bookmarkmanager.bookmarkmanager.db.entity.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @ToString
    public static class SignUpReq{
        private String userId;
        private String userPw;
        private String userEmail;

        public User toEntity(){
            return User.builder()
                    .userId(this.userId)
                    .userPw(this.userPw)
                    .userEmail(this.userEmail)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @ToString
    public static class LoginReq{
        private String userId;
        private String userPw;
    }    

    @Getter
    @AllArgsConstructor
    @Builder
    public static class UpdateReq{
        private String userPw;
        private String userEmail;
    }    

}
