package com.example.pocketmark.service;

import com.example.pocketmark.domain.RefreshToken;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.security.provider.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public RefreshToken createRefreshToken(User user) {

        String jti = UUID.randomUUID().toString();
        String token = jwtUtil.generateRefreshToken(user, jti);

        //TODO: RefreshToken 을 DB로 관리할 경우를 대비한 코드
        //TODO: RefreshToken 상세 스펙은 변경될 수 있음
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token);
        refreshToken.setExpiryDate(jwtUtil.getTokenExpiryFromJWT(token).toInstant());
        refreshToken.setRevoked(false);
        refreshToken.setJti(jti);

        return refreshToken;
    }


    private boolean isTokenNull(RefreshToken refreshToken) {
        return refreshToken == null;
    }
}
