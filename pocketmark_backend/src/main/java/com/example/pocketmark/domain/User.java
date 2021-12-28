package com.example.pocketmark.domain;


import com.example.pocketmark.util.Encryptor;

import org.hibernate.annotations.Where;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Where(clause = "deleted = false")
public class User extends BaseTimeEntity{

    
    @Column(unique = true)
    private String email;

    private String pw;

    @Column(unique = true)
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

    public void changePassword(String newPw){
        this.pw = newPw;
    }
}
