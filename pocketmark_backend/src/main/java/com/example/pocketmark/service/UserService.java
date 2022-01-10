package com.example.pocketmark.service;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.Authority;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.*;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.UserRepository;

import com.example.pocketmark.security.provider.UserPrincipal;
import com.example.pocketmark.util.Encryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final Encryptor encryptor;
    private final UserRepository userRepository;

    @Transactional
    public User create(SignUpUserDto.SignUpDto signUpDto){

        User user = userRepository.save(new User(
                signUpDto.getEmail(),
                encryptor.encrypt(signUpDto.getPw()),
                signUpDto.getNickName()
        ));

        user.setAuthorities(Set.of(new Authority(Authority.ROLE_USER)));
        return user;
    }

    @Transactional
    public void modifyPassword(ModifyPwDto.ChangePwDto changePwDto, Long email) {
        User user = selectUserByToken(email);

        if(!user.isMatch(encryptor, changePwDto.getNowPw())){
            throw new GeneralException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        if(!changePwDto.getConfPw().equals(changePwDto.getNewPw())){
            throw new GeneralException(ErrorCode.DIFFERENT_NEW_PW);
        }

        user.changePassword(encryptor.encrypt(changePwDto.getNewPw()));
    }


    @Transactional
    public User selectUserByLoginReq(LoginDto.LoginReq req){
        Optional<User> savedUser = userRepository.findByEmail(req.getEmail());

        if(savedUser.isPresent()){
            return savedUser.get();
        }else{
            throw new GeneralException(ErrorCode.ENTITY_NOT_EXIST);
        }
    }

    @Transactional
    public User selectUserByToken(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()){
            throw new GeneralException(ErrorCode.ENTITY_NOT_EXIST);
        }

        return user.get();
    }

    @Transactional
    public void modifyNickName(ModifyNickNameDto.ChangeNickNameDto changeNickNameDto, Long email) {
        User user = selectUserByToken(email);

        if(userRepository.existsByNickName(changeNickNameDto.getNewNickName())){
            throw new GeneralException(ErrorCode.NICKNAME_EXIST);
        }

        user.changeNickName(changeNickNameDto.getNewNickName());
    }

    @Transactional
    public void deleteUser(LeaveUser.LeaveUserDto leaveUserDto, Long email) {
        User user = selectUserByToken(email);
        user.deleteUser(leaveUserDto.isLeave());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(
                ()->new GeneralException(ErrorCode.ENTITY_NOT_EXIST)
        );

        return new UserPrincipal(user);
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
