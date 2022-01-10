package com.example.pocketmark.security.filter;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.security.provider.JwtAuthenticationToken;
import com.example.pocketmark.security.provider.JwtUtil;
import com.example.pocketmark.security.provider.UserPrincipal;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationProvider(JwtUtil jwtTokenUtil) {
        this.jwtUtil = new JwtUtil();
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        String jwt = (String) auth.getPrincipal();

        JwtAuthenticationToken jwtAuthenticationToken = null;


        String tokenType = jwtUtil.getTokenTypeFromJWT(jwt);
        boolean tokenValid = false;
        try {
            tokenValid = jwtUtil.validateToken(jwt);
        } catch (Exception e) {
            if(tokenType.equals(JwtUtil.TokenType.REFRESH_TOKEN.toString())) {
                throw new GeneralException(ErrorCode.REFRESH_TOKEN_NOT_VALID);
            } else {
                throw new GeneralException(ErrorCode.ACCESS_TOKEN_NOT_VALID);
            }
        }

        String userId = jwtUtil.getUsernameFromJWT(jwt);
        List<GrantedAuthority> authorities = jwtUtil.getAuthoritiesFromJWT(jwt);

        UserPrincipal principal = new UserPrincipal(userId, authorities);

        jwtAuthenticationToken = new JwtAuthenticationToken(principal, authorities);
        jwtAuthenticationToken.setAuthenticated(tokenValid);

        return jwtAuthenticationToken;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }



}