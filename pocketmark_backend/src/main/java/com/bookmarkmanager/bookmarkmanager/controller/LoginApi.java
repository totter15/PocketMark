package com.bookmarkmanager.bookmarkmanager.controller;



import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bookmarkmanager.bookmarkmanager.configuration.CustomHeaderProvider;
import com.bookmarkmanager.bookmarkmanager.db.entity.User;
import com.bookmarkmanager.bookmarkmanager.db.repository.UserRepository;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto.LoginReq;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto.SignUpReq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.reactive.PreFlightRequestHandler;

@RestController
@RequestMapping("/api")
public class LoginApi{

    @Autowired
    private UserRepository userRepository;

    // login
    @PostMapping("/login")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<String> LogIn(
        HttpServletRequest httpServletRequest,
        @RequestBody LoginReq req
    ){

        //make session
        HttpSession session = httpServletRequest.getSession();

        System.out.println("#Find User : "+req.getUserId());


        //compare requested info with saved info
        
        List<User> user = userRepository.findByUserId(req.getUserId());
        
        if(!user.isEmpty()){
            if(user.get(0).getUserPw().equals(req.getUserPw())){
                if(session.isNew()){
                    session.setAttribute("user_id", user.get(0).getUserId());
                }
                return ResponseEntity.ok().body("true");
            }
            
        }

        return ResponseEntity.ok().body("false");
        
    }


    // sign-up
    @PostMapping("/sign-up")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<String> SignUp(
        @RequestBody SignUpReq req
    ){
        


        System.out.println("#Check req");
        System.out.println(req);


        List<User> userById;
        List<User> userByEmail;
        userById = userRepository.findByUserId(req.getUserId());
        userByEmail = userRepository.findByUserId(req.getUserEmail());
        // 중복됬을때
        if(!userById.isEmpty() || !userByEmail.isEmpty()){
            return ResponseEntity.ok().body("false");
        }else{//중복되지않았을때
            //DB 에 저장
            User user = req.toEntity();
            userRepository.save(user);

            System.out.println("#Sign in new User, So DB check Entirely");
            userRepository.findAll().forEach(System.out::println);
             

            return ResponseEntity.ok().body("true");
        }



    }



}