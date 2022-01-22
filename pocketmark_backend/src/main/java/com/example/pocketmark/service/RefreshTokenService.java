package com.example.pocketmark.service;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.RefreshToken;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.security.provider.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.pocketmark.dto.RefreshToken.*;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public RefreshTokenRes refreshAccessToken(RefreshTokenDto dto) {

        String jwt = dto.getRefreshToken();
        String tokenType = null;
        boolean tokenValid = false;


        try {
            tokenType = jwtUtil.getTokenTypeFromJWT(jwt);
            tokenValid = jwtUtil.validateToken(jwt);
        }catch (MalformedJwtException | UnsupportedJwtException e){
            throw new GeneralException(ErrorCode.IS_NOT_JWT);
        }catch (SignatureException e){
            throw new GeneralException(ErrorCode.NOT_FOUND_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new GeneralException(ErrorCode.REFRESH_TOKEN_NOT_VALID);
        }catch (Exception e){
            throw new GeneralException(ErrorCode.INTERNAL_ERROR);
        }

        //TODO: RefreshToken 의 갱신 여부 추후 협의

        if(tokenType.equals(JwtUtil.TokenType.ACCESS_TOKEN.toString())){
            throw new GeneralException(ErrorCode.IS_TOKEN_DIFFERENT);
        }

        Long jwtSubject = Long.parseLong(jwtUtil.getUsernameFromJWT(jwt));

        String refreshAccessToken = jwtUtil.generateAccessToken(userService.selectUserByUserId(jwtSubject));

        return RefreshTokenRes.builder()
                .accessToken(refreshAccessToken)
                .build();
    }
}
