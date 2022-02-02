package com.example.pocketmark.domain;

import com.example.pocketmark.domain.base.BaseTimeEntityWithId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
public class EmailAuthenticationCode extends BaseTimeEntityWithId {

    private String email;
    private String code;
    private boolean success;

    public void successAuthenticate(){
        this.success = true;
    }
}
