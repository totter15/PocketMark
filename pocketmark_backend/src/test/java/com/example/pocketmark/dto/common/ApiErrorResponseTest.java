package com.example.pocketmark.dto.common;

import com.example.pocketmark.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApiErrorResponse 테스트")
class ApiErrorResponseTest {

    @DisplayName("결과값, 에러코드(Integer), 메시지가 주어졌을 떄 표준 실패 응답을 생성한다.")
    @Test
    public void givenBooleanResultAndIntegerErrorCodeAndMessage_whenCreatingResponse_thenReturnsFailResponse(){
        //Given
        boolean result = false;
        Integer errorCode = 10000;
        String message = "This is test message";

        //When
        ApiErrorResponse response = ApiErrorResponse.of(result,errorCode,message);

        //Then
        then(response)
                .hasFieldOrPropertyWithValue("success",false)
                .hasFieldOrPropertyWithValue("errorCode",ErrorCode.BAD_REQUEST.getCode())
                .hasFieldOrPropertyWithValue("message",message)
                ;

    }


    @DisplayName("결과값, 에러코드가 주어졌을 떄 표준 실패 응답을 생성한다.")
    @Test
    public void givenBooleanResultAndErrorCode_whenCreatingResponse_thenReturnsFailResponse(){
        //Given
        boolean result = false;
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;

        //When
        ApiErrorResponse response = ApiErrorResponse.of(result,errorCode);

        //Then
        then(response)
                .hasFieldOrPropertyWithValue("success",false)
                .hasFieldOrPropertyWithValue("errorCode",ErrorCode.BAD_REQUEST.getCode())
                .hasFieldOrPropertyWithValue("message",errorCode.getMessage())
        ;

    }


    @DisplayName("결과값, 에러코드, 특정 예외가 주어졌을 떄 표준 실패 응답을 생성한다.")
    @Test
    public void givenBooleanResultAndErrorCodeAndAnyException_whenCreatingResponse_thenReturnsFailResponse(){
        //Given
        boolean result = false;
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        Exception e = new Exception("This is test message");


        //When
        ApiErrorResponse response = ApiErrorResponse.of(result,errorCode,e);

        //Then
        then(response)
                .hasFieldOrPropertyWithValue("success",false)
                .hasFieldOrPropertyWithValue("errorCode",ErrorCode.BAD_REQUEST.getCode())
                .hasFieldOrPropertyWithValue("message",errorCode.getMessage(e))
        ;
    }

    @DisplayName("결과값, 에러코드, 메시지가 주어졌을 떄 표준 실패 응답을 생성한다.")
    @Test
    public void givenBooleanResultAndErrorCodeAndMessage_whenCreatingResponse_thenReturnsFailResponse(){
        //Given
        boolean result = false;
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        String message = "This is test message";

        //When
        ApiErrorResponse response = ApiErrorResponse.of(result,errorCode,message);

        //Then
        then(response)
                .hasFieldOrPropertyWithValue("success",false)
                .hasFieldOrPropertyWithValue("errorCode",ErrorCode.BAD_REQUEST.getCode())
                .hasFieldOrPropertyWithValue("message",message)
        ;

    }

}