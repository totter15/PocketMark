package com.example.pocketmark.config;


import com.example.pocketmark.security.filter.*;
import com.example.pocketmark.security.provider.JwtUtil;
import com.example.pocketmark.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(jwtAuthenticationProvider())
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtLoginFilter loginFilter = new JwtLoginFilter(authenticationManager(), userService, jwtUtil);
        JwtCheckFilter checkFilter = new JwtCheckFilter(authenticationManager(), jwtUtil);
        JwtCheckExceptionHandler checkExceptionHandler = new JwtCheckExceptionHandler(authenticationManager(),objectMapper);
        http
                .cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/sign-up","/api/v1/login").permitAll()
                .anyRequest().hasAuthority("ROLE_USER")
        ;

        http
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper));

        http
                .addFilterAt(loginFilter,UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(checkFilter, BasicAuthenticationFilter.class)
        ;

        http
                .addFilterBefore(checkExceptionHandler,JwtCheckFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(jwtUtil);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
