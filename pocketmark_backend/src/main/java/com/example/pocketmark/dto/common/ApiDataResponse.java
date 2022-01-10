package com.example.pocketmark.dto.common;

import com.example.pocketmark.constant.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ApiDataResponse<T> extends ApiErrorResponse {

    private final T data;

    private ApiDataResponse(T data) {
        super(true, ErrorCode.OK.getCode(), ErrorCode.OK.getMessage());
        this.data = data;
    }

    public static <T> ApiDataResponse<T> of(T data) {
        return new ApiDataResponse<>(data);
    }

    public static <T> ApiDataResponse<T> empty() {
        return new ApiDataResponse<>(null);
    }
    
    public static ApiDataResponse<GeneralResponse> success(){
        return new ApiDataResponse<>(new GeneralResponse(true));
    }
    public static ApiDataResponse<GeneralResponse> failed(){
        return new ApiDataResponse<>(new GeneralResponse(false));
    }

    @Getter
    @AllArgsConstructor
    public static class GeneralResponse{
        private boolean success;
    }
    
    @Getter
    @AllArgsConstructor
    public static class JwtTempResponse{
        private boolean success;
        private String accessToken;
    }


}