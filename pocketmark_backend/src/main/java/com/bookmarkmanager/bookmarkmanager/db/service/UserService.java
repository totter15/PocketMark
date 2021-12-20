package com.bookmarkmanager.bookmarkmanager.db.service;

import java.util.List;

import com.bookmarkmanager.bookmarkmanager.db.entity.User;
import com.bookmarkmanager.bookmarkmanager.db.repository.UserRepository;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto.UpdateReq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    public void update(String userId, UpdateReq req){
        User user = userRepository.findByUserId(userId);
        if(user != null){
            user.userUpdate(req.getUserPw(), req.getUserEmail());
            userRepository.save(user);
        }
    } 
    
}
