package com.bookmarkmanager.bookmarkmanager.configuration;

import com.bookmarkmanager.bookmarkmanager.interceptor.AuthInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer{


    private final AuthInterceptor authInterceptor;
    // FilterSecurityInterceptor s;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO Auto-generated method stub
        registry.addInterceptor(authInterceptor);
    }

}
