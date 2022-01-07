package com.example.pocketmark.service;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.Authority;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.LeaveUser;
import com.example.pocketmark.dto.ModifyNickNameDto;
import com.example.pocketmark.dto.ModifyPwDto;
import com.example.pocketmark.dto.SignUpUserDto;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.util.Encryptor;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

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
//        Long userId = 1L;


        if(userId == null){
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }

        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()){
            throw new GeneralException(ErrorCode.ENTITY_NOT_EXIST);
        }

        return user.get();
    }

    @Transactional
    public void modifyNickName(ModifyNickNameDto.ChangeNickNameDto changeNickNameDto, HttpSession session) {
        User user = selectUser(session);

        if(userRepository.existsByNickName(changeNickNameDto.getNewNickName())){
            throw new GeneralException(ErrorCode.NICKNAME_EXIST);
        }

        user.changeNickName(changeNickNameDto.getNewNickName());
    }

    @Transactional
    public void deleteUser(LeaveUser.LeaveUserDto leaveUserDto, HttpSession session) {
        User user = selectUser(session);
        user.deleteUser(leaveUserDto.isLeave());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                ()->new GeneralException(ErrorCode.ENTITY_NOT_EXIST)
        );
    }


    @Transactional
    public void addAuthority(String email, String authority) {
        Optional<User> savedUser = userRepository.findByEmail(email);

        if(savedUser.isPresent()){
            User user = savedUser.get();
            Authority newRole = new Authority(authority);

            if (user.getAuthorities() == null) {
                Set<Authority> authorities = new HashSet<>();
                authorities.add(newRole);
                user.setAuthorities(authorities);

            } else if(user.getAuthorities().contains(newRole)) {
                throw new GeneralException(ErrorCode.ROLE_EXIST);

            } else if (!user.getAuthorities().contains(newRole)) {
                Set<Authority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities());
                authorities.add(newRole);
                user.setAuthorities(authorities);
            }
            userRepository.save(user);
        }else{
            throw new GeneralException(ErrorCode.ENTITY_NOT_EXIST);
        }

    }

    @Transactional
    public void removeAuthority(String email, String authority) {
        Optional<User> savedUser = userRepository.findByEmail(email);

        if (savedUser.isPresent()) {
            User user = savedUser.get();

            if (user.getAuthorities() == null)
                throw new GeneralException(ErrorCode.ROLE_NOT_EXIST);

            Authority targetRole = new Authority(authority);

            if (!user.getAuthorities().contains(targetRole)) {
                throw new GeneralException(ErrorCode.ROLE_NOT_EXIST);
            } else {
                user.setAuthorities(
                        user.getAuthorities().stream().filter(auth -> !auth.equals(targetRole))
                                .collect(Collectors.toSet())
                );

                userRepository.save(user);
            }
        } else {
            throw new GeneralException(ErrorCode.ENTITY_NOT_EXIST);
        }
    }
}
