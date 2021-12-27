package com.example.pocketmark.service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.UserDto;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.util.Encryptor;
import com.google.common.base.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Encryptor encryptor;
    private final UserRepository userRepository;

    @Transactional
    public User create(UserDto.SignUpDto signUpDto){
        return userRepository.save(new User(
                signUpDto.getEmail(),
                encryptor.encrypt(signUpDto.getPw()),
                signUpDto.getNickName()
        ));   
    }
}
