package com.example.pocketmark.security.filter;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.LoginDto;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.security.provider.JwtUtil;
import com.example.pocketmark.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper objectMapper = new ObjectMapper();
    private UserService userService;
    private JwtUtil jwtUtil;

    public JwtLoginFilter(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/login");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException
    {
        LoginDto.LoginReq loginReq = objectMapper.readValue(request.getInputStream(), LoginDto.LoginReq.class);
        if(loginReq.getRefreshToken() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    loginReq.getEmail(), loginReq.getPw(), null
            );
            // user details...
            return getAuthenticationManager().authenticate(token);
        }else{
            boolean verify = jwtUtil.validateToken(loginReq.getRefreshToken());
            if(verify){
                User user = (User) userService.loadUserByUsername(jwtUtil.getUsernameFromJWT(loginReq.getRefreshToken()));
                return new UsernamePasswordAuthenticationToken(
                        user, user.getAuthorities()
                );
            }else{
                throw new GeneralException(ErrorCode.IS_NOT_JWT);
            }
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException
    {
        User user = (User) authResult.getPrincipal();
        response.setHeader("auth_token", jwtUtil.generateAccessToken(user));
        response.setHeader("refresh_token", jwtUtil.generateRefreshToken(user,"sample"));
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(user));
    }
}