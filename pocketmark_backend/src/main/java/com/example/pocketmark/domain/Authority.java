package com.example.pocketmark.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
public class Authority extends BaseTimeEntity implements GrantedAuthority {
    public static final String ROLE_USER = "ROLE_USER";

    public static final Authority USER_AUTHORITY = Authority.builder().authority(ROLE_USER).build();

    private String authority;
}
