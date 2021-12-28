package com.example.pocketmark.service;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.ModifyPwDto;
import com.example.pocketmark.dto.SignUpUserDto;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.util.Encryptor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    public final static String LOGIN_SESSION_KEY = "USER_ID";
    private final Encryptor encryptor;
    private final UserRepository userRepository;

    @Transactional
    public User create(SignUpUserDto.SignUpDto signUpDto){
        return userRepository.save(new User(
                signUpDto.getEmail(),
                encryptor.encrypt(signUpDto.getPw()),
                signUpDto.getNickName()
        ));   
    }

    @Transactional
    public void modifyPassword(ModifyPwDto.ChangePwDto changePwDto, HttpSession session) {
        User user = selectUser(session);

        if(!user.isMatch(encryptor, changePwDto.getNowPw())){
            throw new GeneralException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        if(!changePwDto.getConfPw().equals(changePwDto.getNewPw())){
            throw new GeneralException(ErrorCode.DIFFERENT_NEW_PW);
        }

        user.changePassword(encryptor.encrypt(changePwDto.getNewPw()));
    }

    @Transactional
    public User selectUser(HttpSession session) {
        Long userId = (Long) session.getAttribute(LOGIN_SESSION_KEY);


        if(userId == null){
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }

        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()){
            throw new GeneralException(ErrorCode.ENTITY_NOT_EXIST);
        }

        return user.get();
    }

}
