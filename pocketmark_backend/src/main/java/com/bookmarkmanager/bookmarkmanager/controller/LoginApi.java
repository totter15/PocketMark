package com.bookmarkmanager.bookmarkmanager.controller;



import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookmarkmanager.bookmarkmanager.auth.UserAuthentication;
import com.bookmarkmanager.bookmarkmanager.db.entity.User;
import com.bookmarkmanager.bookmarkmanager.db.repository.UserRepository;
import com.bookmarkmanager.bookmarkmanager.db.service.MailService;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto.LoginReq;
import com.bookmarkmanager.bookmarkmanager.dto.UserDto.SignUpReq;
import com.bookmarkmanager.provider.CustomHeaderProvider;
import com.bookmarkmanager.provider.JwtProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.reactive.PreFlightRequestHandler;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
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
        // System.out.println("#Find User : "+req.getUserId());
        //compare requested info with saved info


        User user = userRepository.findByUserId(req.getUserId());
        
        if(user != null){
            if(user.getUserPw().equals(req.getUserPw())){
                //로그인시 JWT 발급 
                Authentication authentication = new UserAuthentication(req.getUserId(), "test_credential",null);
                String token = JwtProvider.make(authentication);

                return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION,token).body("true");
            }
            
        }

        return ResponseEntity.ok().body("false");
        
    }

    // @Autowired
    // private JavaMailSenderImpl javaMailSenderImpl;
    
    // @PostMapping("/sign-up/email-send")
    // public String sendEmail(
    //     HttpServletRequest req,
    //     @RequestBody String userEmail
    // ){
    //     HttpSession session = req.getSession();
    //     log.info("userEmail : {} ---- [sendEmail]",userEmail);

    //     try {
    //         MailService mailService = new MailService(javaMailSenderImpl);
    //         int numberForVarification = new Random(System.currentTimeMillis()).nextInt(9999);
    //         System.out.println(numberForVarification + "    ----[EMAIL]");
    //         String html = "<p> 인증번호 : "+ numberForVarification +"<p>";
    //         mailService.setEmail("dev1ping9", userEmail, "[PocketMark] 인증번호 입니다", html, true);
    //         mailService.sendMail();

    //         session.setAttribute(userEmail, numberForVarification);
    //         log.info("{} ---- [sendEmail's Session]",session);

    //     } catch (MessagingException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    //     return "SEND";
    // }

    @PostMapping("/sign-up/email-check")
    public String mailCheck(
        HttpServletRequest req,
        @RequestParam String code,
        @RequestParam String userEmail
    ){
        log.info("code : {} , userEmail : {}    -----[email-check]",code,userEmail);
        System.out.println(req.getSession().getAttribute(userEmail) + "   ----[email-check.session's code]");
        if( req.getSession().getAttribute(userEmail) == code ){
            return "OK";
        }
        return "NO";
    }


    // sign-up
    @PostMapping("/sign-up")
    @CrossOrigin(origins = "*", maxAge = 3600)
    
    public ResponseEntity<String> SignUp(
        @RequestBody SignUpReq req
    ){
        


        // System.out.println("#Check req");
        // System.out.println(req);


        User userById;
        User userByEmail;
        userById = userRepository.findByUserId(req.getUserId());
        userByEmail = userRepository.findByUserEmail(req.getUserEmail());
        // 아이디 or email 중복됬을때
        if(userById!=null || userByEmail!=null){
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