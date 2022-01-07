package com.example.pocketmark.dto.common;

import com.example.pocketmark.constant.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiErrorResponse {

    private final Boolean success;
    private final Integer errorCode;
    private final String message;

    public static ApiErrorResponse of(Boolean success, Integer errorCode, String message) {
        return new ApiErrorResponse(success, errorCode, message);
    }

    public static ApiErrorResponse of(Boolean success, ErrorCode errorCode) {
        return new ApiErrorResponse(success, errorCode.getCode(), errorCode.getMessage());
    }

    public static ApiErrorResponse of(Boolean success, ErrorCode errorCode, Exception e) {
        return new ApiErrorResponse(success, errorCode.getCode(), errorCode.getMessage(e));
    }

    public static ApiErrorResponse of(Boolean success, ErrorCode errorCode, String message) {
        return new ApiErrorResponse(success, errorCode.getCode(), errorCode.getMessage(message));
    }

}