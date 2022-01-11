package com.example.pocketmark.security.filter;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.security.provider.JwtAuthenticationToken;
import com.example.pocketmark.security.provider.JwtUtil;
import com.example.pocketmark.security.provider.UserPrincipal;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;



public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationProvider(JwtUtil jwtTokenUtil) {
        this.jwtUtil = new JwtUtil();
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        String jwt = (String) auth.getPrincipal();

        JwtAuthenticationToken jwtAuthenticationToken = null;


        String tokenType = null;
        boolean tokenValid = false;


        try {
            tokenType = jwtUtil.getTokenTypeFromJWT(jwt);
            tokenValid = jwtUtil.validateToken(jwt);
        }catch (MalformedJwtException | UnsupportedJwtException e){
            throw new GeneralException(ErrorCode.IS_NOT_JWT);
        }catch (SignatureException e){
            throw new GeneralException(ErrorCode.NOT_FOUND_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new GeneralException(ErrorCode.TOKEN_NOT_VALID);
        }catch (Exception e){
            throw new GeneralException(ErrorCode.INTERNAL_ERROR);
        }

        String userId = jwtUtil.getUsernameFromJWT(jwt);
        List<GrantedAuthority> authorities = null;

        if(tokenType.equals(JwtUtil.TokenType.ACCESS_TOKEN.toString())){
            authorities = jwtUtil.getAuthoritiesFromJWT(jwt);
            authorities.forEach(i-> System.out.println(i.getAuthority()));
        }

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