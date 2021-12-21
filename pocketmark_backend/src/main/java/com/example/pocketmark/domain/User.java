package com.example.pocketmark.domain;

import javax.persistence.Entity;

import com.example.pocketmark.domain.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity{
    private String email;
    private String pw;
}
