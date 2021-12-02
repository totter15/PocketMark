package com.bookmarkmanager.bookmarkmanager.configuration.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpFilter implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        

        chain.doFilter(request, response);
        
        HttpServletResponse res = (HttpServletResponse) response;
        
        // res.setHeader("Access-Control-Allow-Origin","*");
        // res.setHeader("Access-Control-Allow-Methods","GET");
        // res.setHeader("Access-Control-Allow-Headers","Content-Type, Authorization, Content-Length, X-Requested-With");

        
    }

    
}
