package com.bookmarkmanager.bookmarkmanager.dto;

import com.bookmarkmanager.bookmarkmanager.db.entity.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    @Getter
    @NoArgsConstructor
    public static class SignUpReq{
        private String userId;
        private String userPw;
        private String userEmail;

        @Builder
        public SignUpReq(String id, String pw, String email){
            this.userId = id;
            this.userPw = pw;
            this.userEmail = email; 
        }

        public User toEntity(){
            return User.builder()
                    .userId(this.userId)
                    .userPw(this.userPw)
                    .userEmail(this.userEmail)
                    .build();
        }
    }




    @Getter
    @NoArgsConstructor
    public static class LoginReq{
        private String id;
        private String pw;
        
        @Builder
        public LoginReq(String id, String pw){
            this.id=id;
            this.pw=pw;
        }
    }



    
    
}
