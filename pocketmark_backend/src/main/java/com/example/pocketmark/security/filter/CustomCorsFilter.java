package com.example.pocketmark.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CustomCorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // TODO Auto-generated method stub

        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        String[] allowedDomainList = {"http://localhost:3000"
                                    ,"https://pocketmark.site"
                                    // ,"https://back.pocketmark.site"
                                    };
        

        String origin = req.getHeader("Origin");
        boolean flag = false;
        if(origin !=null){
            for(String domain : allowedDomainList){
                // System.out.println("origin : " + origin);
                // System.out.println("domain : " + domain);
                if(origin.equals(domain)){
                    flag=true; 
                    break;
                }
            }
        }

        if(flag){
            res.setHeader("Access-Control-Allow-Origin", origin);
            res.setHeader("Access-Control-Allow-Method", "*");
            res.setHeader("Access-Control-Allow-Headers", "*");
            res.setHeader("Access-Control-Allow-Max-Age", "3600");    
        }
        chain.doFilter(request, response);
        // System.out.println("> First Filter.getHeaderNames() : " + res.getHeaderNames());
        return;
        
        
         
    }
    
}
