package com.bookmarkmanager.bookmarkmanager.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseDto {
    String errorCode;
    String message;
    
}
