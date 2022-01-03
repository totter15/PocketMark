package com.example.pocketmark.service;

import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.SignUpUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class LoginService {

    public final static String LOGIN_SESSION_KEY = "USER_ID";
    private final UserService userService;

    @Transactional
    public void signUp(SignUpUserDto.SignUpDto signUpDto, HttpSession session){
        User user = userService.create(signUpDto);
        session.setAttribute(LOGIN_SESSION_KEY,user.getId());
    }
    
}
