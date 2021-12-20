package com.bookmarkmanager.bookmarkmanager.db.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.bookmarkmanager.bookmarkmanager.db.entity.base.DbEntity;
import com.bookmarkmanager.bookmarkmanager.db.repository.UserRepository;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto.SignUpReq;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto.UpdateReq;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Where;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Builder
@Where(clause = "deleted = false")
public class User extends DbEntity{
    private String userId;
    private String userPw;
    private String userEmail;

    @OneToMany(fetch = FetchType.LAZY, 
               cascade = {CascadeType.REMOVE,
                         CascadeType.DETACH,
                         CascadeType.MERGE}
                  )
    @JoinColumn(name="user_no")
    // @BatchSize(size=100)
    @ToString.Exclude
    @Builder.Default
    private List<Folder> folders = new ArrayList<>();



    // @OneToMany
    // private List<Folder> folders;

    // @Builder
    // public User(String userId, String userPw, String userEmail){
    //     this.userId= userId;
    //     this.userPw = userPw;
    //     this.userEmail = userEmail;
    // }

    public void userUpdate(String pw, String email){
        this.userPw = pw;
        this.userEmail = email;
    }

    public SignUpReq toSignUpReq(){
        return SignUpReq.builder()
                    .userId(userId)
                    .userEmail(userEmail)
                    .userPw(userPw)
                    .build();
    }

}
