package com.example.pocketmark.service;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.RefreshToken;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.LoginDto;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.security.provider.JwtUtil;
import com.example.pocketmark.security.provider.TokenBox;
import com.example.pocketmark.security.provider.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public TokenBox authenticate(LoginDto.LoginReq req) {
        Authentication auth = authenticate(req.getEmail(),req.getPw());

        final UserDetails userDetails = (UserDetails) auth.getPrincipal();
        UserPrincipal userPrincipal = (UserPrincipal) userDetails;
        User user = userPrincipal.getUser();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        String accessToken  = jwtUtil.generateAccessToken(user);

        return TokenBox.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .accessToken(accessToken)
                .build();
    }

    private Authentication authenticate(String email, String password){
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);

        try{
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        }catch (DisabledException e){
            throw new GeneralException(ErrorCode.USER_INACTIVE);
        }catch (BadCredentialsException e){
            throw new GeneralException(ErrorCode.EMAIL_OR_PASSWORD_NOT_MATCH);
        }catch (Exception e){
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }

    }
}
