package com.example.pocketmark.service;

import java.nio.file.attribute.UserPrincipal;

import com.example.pocketmark.domain.SnsUser;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.repository.SnsUserRepository;
import com.example.pocketmark.repository.UserRepository;
import com.google.common.base.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SnsUserService {
    
    private final SnsUserRepository snsUserRepository;
    private final UserRepository userRepository;

    public User load(SnsUser oAuth2User){
        Optional<SnsUser> snsUser = snsUserRepository
            .findByoauth2UserId(oAuth2User.getOauth2UserId());
        if(snsUser==null){
            //userRepo 에 등록 
            User user = User.builder()
                        .email(oAuth2User.getEmail())
                        .nickName(oAuth2User.getName())
                        .build();
            //이메일 중복시 리다이렉트 구현필요
            //닉네임 중복시 1,2,3,4 번호 부여 구현필요
            user = userRepository.save(user);
            
            snsUserRepository.save(
                SnsUser.builder().userId(user.getId()).build()
            );

            return user; 
        }
        else{
            //userRepo에서 꺼내오기
            return userRepository.findById(snsUser.get().getUserId()).get();
        }
            

    }
}
