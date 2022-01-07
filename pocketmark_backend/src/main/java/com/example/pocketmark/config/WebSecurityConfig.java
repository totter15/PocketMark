package com.example.pocketmark.config;


import com.example.pocketmark.security.filter.JwtCheckFilter;
import com.example.pocketmark.security.filter.JwtLoginFilter;
import com.example.pocketmark.service.UserService;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import lombok.RequiredArgsConstructor;


@EnableWebSecurity(debug=false)
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtLoginFilter loginFilter = new JwtLoginFilter(authenticationManager(), userService);
        JwtCheckFilter checkFilter = new JwtCheckFilter(authenticationManager(), userService);

        http
                .cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/sign-up","/api/v1/login").permitAll()
                .anyRequest().authenticated()
        ;

        http
                .addFilterAt(loginFilter,UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(checkFilter, BasicAuthenticationFilter.class)
        ;

    }

}
