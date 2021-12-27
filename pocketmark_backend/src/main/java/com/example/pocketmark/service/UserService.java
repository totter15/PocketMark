package com.example.pocketmark.service;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.UserDto;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.util.Encryptor;
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
        userRepository
                .findByEmail(signUpDto.getEmail())
                .ifPresent(u->{
                    throw new GeneralException(ErrorCode.EMAIL_EXIST);
                });

        userRepository
                .findByNickName(signUpDto.getNickName())
                .ifPresent(u->{
                    throw new GeneralException(ErrorCode.NICKNAME_EXIST);
                });


        return userRepository.save(new User(
                signUpDto.getEmail(),
                encryptor.encrypt(signUpDto.getPw()),
                signUpDto.getNickName()
        ));
    }
}
