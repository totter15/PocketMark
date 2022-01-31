package com.example.pocketmark.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;

import com.example.pocketmark.domain.base.BaseTimeEntity;
import com.example.pocketmark.domain.base.BaseTimeEntityWithId;

import static java.util.Objects.hash;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
public class Authority extends BaseTimeEntityWithId implements GrantedAuthority {
    public static final String ROLE_USER = "USER";

    public static final Authority USER_AUTHORITY = Authority.builder().authority(ROLE_USER).build();

    private String authority;

    @Override
    public int hashCode() {
        return hash(authority);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Authority){
            Authority objAuthority = (Authority) obj;
            return this.authority.equals(objAuthority.getAuthority());
        }
        return false;
    }
}
