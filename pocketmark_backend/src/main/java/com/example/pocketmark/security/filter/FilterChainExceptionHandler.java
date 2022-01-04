package com.example.pocketmark.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.pocketmark.constant.ErrorCode;
import com.google.common.net.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FilterChainExceptionHandler extends OncePerRequestFilter{


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        try{
            filterChain.doFilter(request, response);
        }catch(Exception e){
            log.error("Security Filter Chain Exception : {}\nE.message : {}", e.getClass(),e.getMessage());
            
            // response.setStatus(ErrorCode.UNAUTHORIZED.getHttpStatus().value());
            response.setContentType("application/json");
            response.sendError(ErrorCode.UNAUTHORIZED.getHttpStatus().value(), e.getMessage());
            
        }
        
    }
    
}
