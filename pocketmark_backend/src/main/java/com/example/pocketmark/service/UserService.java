package com.example.pocketmark.service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

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
    public User create(UserDto.signUpRequest signUpReq){
        // userRepository
        //         .findByEmail(signUpReq.getEmail())
        //         .ifPresent(u->{
        //             throw new GeneralException(ErrorCode.EMAIL_EXIST);
        //         });

        // userRepository
        //         .findByNickName(signUpReq.getNickName())
        //         .ifPresent(u->{
        //             throw new GeneralException(ErrorCode.NICKNAME_EXIST);
        //         });
        
        try{
            return userRepository.save(new User(
                signUpReq.getEmail(),
                encryptor.encrypt(signUpReq.getPw()),
                signUpReq.getNickName()
            ));
        }catch(Exception e){
            System.out.println("Unique constraint");
            return null;
        }

        
    }
}
