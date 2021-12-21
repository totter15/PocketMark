package com.example.pocketmark.domain;

import com.example.pocketmark.util.Encryptor;
import lombok.*;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class User extends BaseTimeEntity{

    private String email;
    private String pw;
    private String nickName;

    public User(String email, String pw, String nickName){
        this.email = email;
        this.pw = pw;
        this.nickName = nickName;
        this.setDeleted(false);
    }

    public boolean isMatch(Encryptor encryptor, String password){
        return encryptor.isMatch(password,this.pw);
    }
}