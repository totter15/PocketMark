package com.example.pocketmark.security.filter;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.dto.common.ApiErrorResponse;
import com.example.pocketmark.exception.GeneralException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class JwtCheckExceptionHandler extends BasicAuthenticationFilter {

    ObjectMapper objectMapper;

    public JwtCheckExceptionHandler(AuthenticationManager authenticationManager,
                                    ObjectMapper objectMapper) {
        super(authenticationManager);
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try{
            chain.doFilter(request,response);
        }catch (GeneralException e){
            setErrorResponse(e.getErrorCode(), response);
        }
    }

    public void setErrorResponse(ErrorCode errorCode, HttpServletResponse response){
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json");
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.of(false, errorCode);

        try{
            String json = objectMapper.writeValueAsString(apiErrorResponse);
            response.getWriter().write(json);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
