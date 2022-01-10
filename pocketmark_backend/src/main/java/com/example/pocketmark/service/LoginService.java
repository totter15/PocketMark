package com.example.pocketmark.service;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.SignUpUserDto;
import com.example.pocketmark.dto.LoginDto.LoginReq;
import com.example.pocketmark.dto.common.ApiDataResponse;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.UserRepository;

import com.example.pocketmark.security.provider.JwtUtil;
import com.example.pocketmark.security.provider.TokenBox;
import com.example.pocketmark.util.Encryptor;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserService userService;
    private final Encryptor encryptor;

    @Transactional
    public User signUp(SignUpUserDto.SignUpDto signUpDto){
        return userService.create(signUpDto);
    }

}
