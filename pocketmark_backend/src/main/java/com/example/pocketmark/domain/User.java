package com.example.pocketmark.domain;


import com.example.pocketmark.util.Encryptor;
import org.hibernate.annotations.Where;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@Where(clause = "deleted = false")
public class User extends BaseTimeEntity{

    
    @Column(unique = true)
    private String email;

    private String pw;

    @Column(unique = true)
    private String nickName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    @Setter
    private Set<Authority> authorities;

    private boolean active;


    public User(String email, String pw, String nickName){
        this.email = email;
        this.pw = pw;
        this.nickName = nickName;
        this.active = true;
        this.setDeleted(false);
    }


    public boolean isMatch(Encryptor encryptor, String password){
        return encryptor.isMatch(password,this.pw);
    }

    public void changePassword(String newPw){
        this.pw = newPw;
    }

    public void changeNickName(String newNickName){
        this.nickName = newNickName;
    }

    public void deleteUser(boolean deleted){
        this.setDeleted(deleted);
    }


}
