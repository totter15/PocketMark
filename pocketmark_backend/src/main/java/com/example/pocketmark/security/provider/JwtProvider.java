package com.example.pocketmark.security.provider;


import java.time.Duration;
import java.util.Date;

import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtProvider {
    //JWT : 이름, 만료시기, 권한, 이메일발송여부 이런거 넣어도됨 ㅇㅇ (PW는 안됨 )
    private static final String JWT_SECRET = "whoKnowsMyKey?blahblah"; //Key 털리면 끝임  
    // private static final long JWT_EXPIRATION_MILLS = Duration.ofMinutes(20).toMillis();
    private static final long JWT_EXPIRATION_MILLS = Duration.ofSeconds(3).toMillis();


    public static String make(Authentication authentication, String dbId){
        Date now = new Date();

        String token = Jwts.builder()
                            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                            .setSubject((String)authentication.getPrincipal())
                            .setIssuedAt(now)
                            .setExpiration(new Date(now.getTime()+JWT_EXPIRATION_MILLS)) 
                            .claim("userId", dbId)
                            .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                            .compact();
        return token;
    }
    


    public static Claims getClaims(String token){
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
    }

    public static String getUserCoded(String token){
        return (String)Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().get("code");
    }

    public static boolean validate(String token){
        try{
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        }catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;

    }
}

