package com.example.pocketmark.util;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptEncryptor implements Encryptor{
    @Override
    public String encrypt(String origin) {
        return BCrypt.hashpw(origin,BCrypt.gensalt());
    }

    @Override
    public boolean isMatch(String origin, String hashed) {
        return BCrypt.checkpw(origin,hashed);
    }
}
