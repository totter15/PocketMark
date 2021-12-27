package com.example.pocketmark.service;

import com.example.pocketmark.dto.UserDto;
import com.example.pocketmark.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserService userService;

    @Transactional
    public void signUp(UserDto.SignUpDto signUpDto){
        userService.create(signUpDto);
    }
    
}
