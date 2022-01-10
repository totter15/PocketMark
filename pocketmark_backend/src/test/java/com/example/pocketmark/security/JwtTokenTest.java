package com.example.pocketmark.security;

import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
public class JwtTokenTest {




//    @Test
//    void encodeTest(){
//        String jwt = Jwts.builder()
//        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
//        .setSubject("fresh")
//        .claim("email", "Dasd")
//        .claim("code", "asdasd")
//        .signWith(SignatureAlgorithm.HS256, "SunDSADASD")
//        .compact();
//
//        System.out.println(jwt);
//        System.out.println(">>>");
//        String[] tokens = jwt.split("\\.");
//        System.out.println(new String(Base64.getDecoder().decode(tokens[1])));

//    }
}
