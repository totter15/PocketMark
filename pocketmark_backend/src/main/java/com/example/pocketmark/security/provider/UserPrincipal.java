package com.example.pocketmark.security.provider;

import com.example.pocketmark.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private Collection<? extends GrantedAuthority> authorities;
    private String username;
    private String password;
    private boolean active;

    private User user;

    public UserPrincipal(String userId, Collection<? extends GrantedAuthority> authorities) {
        this.username = userId;
        this.authorities = authorities;
        this.password = null;
        this.active = true;
    }

    public UserPrincipal(User user) {
        this.username = user.getId().toString();
        this.password = user.getPw();
        this.active = user.isActive();
        this.authorities = user.getAuthorities().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());

        this.user = user;
    }

    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }

    @JsonIgnore
    public User getUser(){
        return user;
    }

}
