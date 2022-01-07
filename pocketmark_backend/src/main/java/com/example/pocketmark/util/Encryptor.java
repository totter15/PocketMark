package com.example.pocketmark.util;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Encryptor{

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encrypt(String origin) {
        return passwordEncoder.encode(origin);
    }

    public boolean isMatch(String origin, String hashed) {
        return passwordEncoder.matches(origin,hashed);
    }
}
