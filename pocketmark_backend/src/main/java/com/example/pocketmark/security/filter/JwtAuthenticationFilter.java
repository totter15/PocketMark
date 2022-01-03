package com.example.pocketmark.security.filter;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.security.authentication.UserAuthentication;
import com.example.pocketmark.security.provider.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private String getJwtFromRequest(HttpServletRequest req){
        String bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
        // log.info(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        log.info("req URL : {}   ----[JwtAuthFilter]", req.getRequestURI());
        
        
        if(req.getRequestURI().contains("api/v1/sign-up")
            || req.getRequestURI().equals("/api/v1/login")){
            filterChain.doFilter(request, response);
            return; // to block backpropagation(not excute logic below)
        }
        
    
        try{
            String jwt = getJwtFromRequest(req);
            if(StringUtils.hasText(jwt) && JwtProvider.validate(jwt)){
                String userId = JwtProvider.getClaims(jwt).getSubject();
                log.info("after jwt : userid = {}   ----[JwtAuthFilter]",userId);
                //인증객체 만들기
                UserAuthentication authentication = new UserAuthentication(userId, "test_credential",null); 
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                if(!StringUtils.hasText(jwt)){
                    throw new GeneralException(ErrorCode.UNAUTHORIZED);
                }
                if(!JwtProvider.validate(jwt)){
                    throw new GeneralException(ErrorCode.JWT_EXPIRED);
                }
            }


        }catch(Exception e){
            log.error("Couldn't see user Auth in security context.");
        }
        
        filterChain.doFilter(request, response);

        
        
    }
    
}

