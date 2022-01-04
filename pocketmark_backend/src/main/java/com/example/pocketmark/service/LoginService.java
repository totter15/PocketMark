package com.example.pocketmark.service;

import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.SignUpUserDto;
import com.example.pocketmark.dto.LoginDto.LoginReq;
import com.example.pocketmark.dto.common.ApiDataResponse;
import com.example.pocketmark.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
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



    @Autowired UserRepository userRepository;
    public Long login(LoginReq req){
        var item = userRepository.findByEmail(req.getEmail());
        if(item.isPresent()){
            return item.get().getId();
        }

        return null;
    }

    
}
