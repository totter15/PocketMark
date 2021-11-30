package com.bookmarkmanager.bookmarkmanager.controller;



import java.util.List;

import com.bookmarkmanager.bookmarkmanager.configuration.CustomHeaderProvider;
import com.bookmarkmanager.bookmarkmanager.db.entity.User;
import com.bookmarkmanager.bookmarkmanager.db.repository.UserRepository;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto.LoginReq;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto.SignUpReq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginApi{

    @Autowired
    private UserRepository userRepository;

    // login
    @PostMapping("/login")
    public ResponseEntity<String> LogIn(
        @RequestBody LoginReq req
    ){

        
        //set Header to pass CORS policy
        CustomHeaderProvider CustomHeaderProvider = new CustomHeaderProvider();
        CustomHeaderProvider.setBasicCORS();



        //compare requested info with saved info
        
        List<User> user = userRepository.findByUserId(req.getId());
        
        if(!user.isEmpty()){
            if(user.get(0).getUserPw().equals(req.getPw())){
                return ResponseEntity.ok().headers(CustomHeaderProvider.getHttpHeaders()).body("true");
            }
            
        }

        return ResponseEntity.ok().headers(CustomHeaderProvider.getHttpHeaders()).body("false");
        
    }


    // sign-up
    @PostMapping("/sign-up")
    public ResponseEntity<String> SignUp(
        @RequestBody SignUpReq req
    ){
        //set Header to pass CORS policy
        CustomHeaderProvider CustomHeaderProvider = new CustomHeaderProvider();
        CustomHeaderProvider.setBasicCORS();


        List<User> userById;
        List<User> userByEmail;
        userById = userRepository.findByUserId(req.getUserId());
        userByEmail = userRepository.findByUserId(req.getUserEmail());
        // 중복됬을때
        if(!userById.isEmpty() || !userByEmail.isEmpty()){
            return ResponseEntity.ok().headers(CustomHeaderProvider.getHttpHeaders()).body("false");
        }else{//중복되지않았을때
            //DB 에 저장
            User user = req.toEntity();
            userRepository.save(user);

            System.out.println("#DB check");
            userRepository.findAll().forEach(System.out::println);
             

            return ResponseEntity.ok().headers(CustomHeaderProvider.getHttpHeaders()).body("true");
        }


    }



}