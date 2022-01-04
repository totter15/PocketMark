package com.example.pocketmark.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.pocketmark.domain.SnsUser;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.security.authentication.UserAuthentication;
import com.example.pocketmark.service.SnsUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    SnsUserService snsUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // TODO Auto-generated method stub

        Object principal = authentication.getPrincipal();
        if(principal instanceof OidcUser){
            //google
            SnsUser oauth = SnsUser.Provider.google.convert((OidcUser)principal);
            User user = snsUserService.load(oauth);
            SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(user.getNickName(), null,null));
            
        }else if(principal instanceof OAuth2User){
            //naver
            SnsUser oauth = SnsUser.Provider.naver.convert((OAuth2User)principal);
            User user = snsUserService.load(oauth);
            SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(user.getNickName(), null,null));
            
        }


        //rootpage redirection (프론트에서 구현해야함)
        // request.getRequestDispatcher("/").forward(request, response);
        
    }
    
}
