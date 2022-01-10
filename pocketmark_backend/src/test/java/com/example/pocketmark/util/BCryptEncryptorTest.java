package com.example.pocketmark.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

@DisplayName("Encryptor 검증 테스트")
class BCryptEncryptorTest {
    @DisplayName("문자열을 암호화 하여 해당 문자열과 암호화된 문자열이 맞는지 확인한다.")
    @Test
    public void givenAnyString_whenEncrypt_thenIsMatchTrue(){
        //Given
        String origin = "test";
        Encryptor encryptor = new Encryptor();

        //When
        String hashPw = encryptor.encrypt(origin);

        //Then
        then(encryptor.isMatch(origin,hashPw)).isTrue();
    }

    @DisplayName("문자열을 암호화 하여 다른 문자열과 암호화된 문자열이 틀림을 확인한다.")
    @Test
    public void givenAnyString_whenEncrypt_thenIsMatchFalse(){
        //Given
        String origin = "test";
        Encryptor encryptor = new Encryptor();

        //When
        String hashPw = encryptor.encrypt(origin);

        //Then
        then(encryptor.isMatch(origin+"1",hashPw)).isFalse();
    }

}