package com.bookmarkmanager.bookmarkmanager.db.entity;

import javax.persistence.Entity;

import com.bookmarkmanager.bookmarkmanager.dto.UserDto;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto.SignUpReq;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // new User() 막음 
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends DbEntity{
    private String userId;
    private String userPw;
    private String userEmail;

    @Builder
    public User(String userId, String userPw, String userEmail){
        this.userId= userId;
        this.userPw = userPw;
        this.userEmail = userEmail;
    }

    public void updateUser(SignUpReq req){
        this.userEmail = req.getUserEmail();
        this.userPw = req.getUserPw();
    }

    




}
