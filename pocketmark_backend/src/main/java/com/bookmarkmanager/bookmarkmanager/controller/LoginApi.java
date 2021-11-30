package com.bookmarkmanager.bookmarkmanager.controller;



import com.bookmarkmanager.bookmarkmanager.configuration.CustomHeaderProvider;
import com.bookmarkmanager.bookmarkmanager.db.login.User;
import com.bookmarkmanager.bookmarkmanager.db.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginApi{

    @Autowired
    private UserRepository userRepository;

    // login
    @GetMapping("/login")
    public ResponseEntity<String> LogIn(
        @RequestParam String id,
        @RequestParam String pw
    ){


        System.out.println("id : "+ id);
        System.out.println("pw : "+ pw);
        
        //set Header to pass CORS policy
        CustomHeaderProvider CustomHeaderProvider = new CustomHeaderProvider();
        CustomHeaderProvider.setBasicCORS();


        System.out.println("#User Repo");
        userRepository.findAll().forEach(System.out::println);

        //compare requested info with saved info
        
        User user = userRepository.findByUserId(id);
        
        if(user!=null){
            if(user.getUserPw().equals(pw)){
                return ResponseEntity.ok().headers(CustomHeaderProvider.getHttpHeaders()).body("true");
            }
            
        }

        return ResponseEntity.ok().headers(CustomHeaderProvider.getHttpHeaders()).body("false");
        
    }


    // sign-in
    @GetMapping("/sign-in")
    public ResponseEntity<String> SignIn(
        @RequestParam String id,
        @RequestParam String email
    ){
        //set Header to pass CORS policy
        CustomHeaderProvider CustomHeaderProvider = new CustomHeaderProvider();
        CustomHeaderProvider.setBasicCORS();


        User user;
        user = userRepository.findByUserId(id);
        if(user!=null){
            return ResponseEntity.ok().headers(CustomHeaderProvider.getHttpHeaders()).body("false");
        }
        user = userRepository.findByEmail(email);
        if(user!=null){
            return ResponseEntity.ok().headers(CustomHeaderProvider.getHttpHeaders()).body("false");
        }
        return ResponseEntity.ok().headers(CustomHeaderProvider.getHttpHeaders()).body("true");

    }



}