package com.example.pocketmark.security.filter;


import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.security.provider.JwtAuthenticationToken;
import com.example.pocketmark.security.provider.JwtUtil;
import com.example.pocketmark.security.provider.VerifyResult;
import com.example.pocketmark.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



public class JwtCheckFilter extends BasicAuthenticationFilter {

    private JwtUtil jwtUtil;

    public JwtCheckFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearer == null || !bearer.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String token = bearer.substring("Bearer ".length());

        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token);
        Authentication authentication = getAuthenticationManager().authenticate(jwtAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

}

