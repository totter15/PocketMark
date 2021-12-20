package com.bookmarkmanager.bookmarkmanager.configuration.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookmarkmanager.bookmarkmanager.auth.UserAuthentication;
import com.bookmarkmanager.bookmarkmanager.dto.ErrorResponseDto;
import com.bookmarkmanager.provider.JwtProvider;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private String getJwtFromRequest(HttpServletRequest req){
        String bearerToken = req.getHeader("Authorization");
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
        
        
        if(req.getRequestURI().contains("api/sign-up")
            || req.getRequestURI().equals("/api/login")){
            filterChain.doFilter(request, response);
            return; // to block backpropagation(not excute logic below)
        }
        
    
        try{
            String jwt = getJwtFromRequest(req);
            if(StringUtils.hasText(jwt) && JwtProvider.validate(jwt)){
                String userId = JwtProvider.getUserId(jwt);
                log.info("after jwt : userid = {}   ----[JwtAuthFilter]",userId);
                //인증객체 만들기
                UserAuthentication authentication = new UserAuthentication(userId, "test_credential",null); 
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                // 인증객체를 context에 등록 // 등록 후 뒷단 필터에서 인증작업 후, clear 한다. 
                // 여러사용자가 동시에 인증시도 한다면? //멀티쓰레드로 나눠서 관리하나? 아니면 인증쓰레드는 단일인가?
                // 어느정도 부하가 오기전에 단일이라 가정 -> Holder는 하나기 때문에 clear 시기에 따라 인증못하는 사용자가 발생...
                // 멀티인가,,, clear하는 이유는 메모리관리? 흠,,,
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                if(!StringUtils.hasText(jwt)){
                    request.setAttribute("unauthorization", "401-01 인증키 없음.");
                    response.setStatus(401);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    
                    ObjectMapper mapper = new ObjectMapper();
                    response.getWriter().write(mapper.writeValueAsString(ErrorResponseDto.builder().errorCode("401-01").message("JWT 인증키 없음.").build()));
                    return;
                }
                if(!JwtProvider.validate(jwt)){
                    request.setAttribute("unauthorization", "401-02 인증키 만료(유효하지않음).");
                    response.setStatus(401);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    ObjectMapper mapper = new ObjectMapper();
                    response.getWriter().write(mapper.writeValueAsString(ErrorResponseDto.builder().errorCode("401-02").message("인증키 만료(유효하지않음).").build()));
                    return;
                }
            }


        }catch(Exception e){
            log.error("Couldn't see user Auth in security context.");
        }
        
        filterChain.doFilter(request, response);

        
        
    }
    
}
