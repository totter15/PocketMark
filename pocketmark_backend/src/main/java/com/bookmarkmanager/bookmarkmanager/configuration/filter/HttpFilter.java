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
import org.springframework.web.util.ContentCachingResponseWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpFilter implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        // ContentCachingResponseWrapper res = (ContentCachingResponseWrapper) response;
        
        //이거 왜 작동하는거지? 필터에 집어넣는건 res 가 아니라 response인데...
        //객체니까 주소값이 들어가고 참조에 의한 접근이 되는것임 ㅇㅇ
        // dofilter이후에는 
        res.setHeader("thisisTest", "whatup"); 
        System.out.println("# req's header");
        System.out.println(req.getHeaderNames());
        
        
        
        chain.doFilter(request, response);
        // after filter


        System.out.println("# res's header");
        System.out.println(res.getHeaderNames());
        System.out.println(res.getHeader("Set-Cookie"));




        // String corsOrigin = "http://localhost:3000";
        // String corsMethod = "GET, POST";
        
        // res.copyBodyToResponse();
        // res.setStatus(500);
        
        System.out.println("filter working");
        
        // System.out.println("#res header");
        // System.out.println(res.getHeaderNames());
        // System.out.println(res.getHeader("Access-Control-Allow-Origin"));
        // System.out.println(res.getHeader("Vary"));
        
        // res.setHeader("Access-Control-Allow-Origin",corsOrigin);
        // res.setHeader("Access-Control-Allow-Methods",corsMethod);
        // res.setHeader("Access-Control-Allow-Headers","Content-Type, Authorization, Content-Length, X-Requested-With");
        // res.setHeader("Access-Control-Max-Age","3600");
        
        
    }

    
}
