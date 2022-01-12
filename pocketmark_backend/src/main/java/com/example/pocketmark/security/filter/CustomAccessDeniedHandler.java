package com.example.pocketmark.security.filter;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.dto.common.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    ObjectMapper objectMapper;

    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
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
