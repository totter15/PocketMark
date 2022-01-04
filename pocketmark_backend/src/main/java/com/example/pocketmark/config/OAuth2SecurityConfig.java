package com.example.pocketmark.config;

import com.example.pocketmark.security.service.OAuth2UserService;
import com.example.pocketmark.security.service.MyOidcUserService;
import com.example.pocketmark.security.service.OAuth2SuccessHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// @EnableWebSecurity
public class OAuth2SecurityConfig extends WebSecurityConfigurerAdapter{
    
    // @Autowired
    // private OAuth2UserService oAuth2UserService;
    // @Autowired
    // private MyOidcUserService oidcUserService;

    // @Autowired
    // private OAuth2SuccessHandler oAuth2SuccessHandler;


    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     http
    //         .oauth2Login(oauth2->oauth2.userInfoEndpoint(
    //             userInfo->userInfo.userService(oAuth2UserService)
    //             .oidcUserService(oidcUserService)
                
    //             ).successHandler(oAuth2SuccessHandler)
            
    //         )
            
            
    //         ;
    // }

}
