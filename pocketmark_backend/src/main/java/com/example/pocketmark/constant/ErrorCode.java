package com.example.pocketmark.constant;

import com.example.pocketmark.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    OK(0, HttpStatus.OK, "Ok"),

    BAD_REQUEST(10000, HttpStatus.BAD_REQUEST, "Bad request"),
    SPRING_BAD_REQUEST(10001, HttpStatus.BAD_REQUEST, "Spring-detected bad request"),
    VALIDATION_ERROR(10002, HttpStatus.BAD_REQUEST, "Validation error"),
    NOT_FOUND(10003, HttpStatus.NOT_FOUND, "Requested resource is not found"),
    EMAIL_EXIST(1004,HttpStatus.BAD_REQUEST,"Duplicate email"),
    NICKNAME_EXIST(1005,HttpStatus.BAD_REQUEST,"Duplicate NickName"),
    EMAIL_OR_NICKNAME_EXIST(1006,HttpStatus.CONFLICT,"Duplicate Emial or NickName"),
    ENTITY_NOT_EXIST(1007,HttpStatus.BAD_REQUEST,"Entity not exist"),
    UNAUTHORIZED(1008,HttpStatus.UNAUTHORIZED,"Unauthorized. Retry Login."),
    PASSWORD_NOT_MATCH(1009,HttpStatus.BAD_REQUEST,"Please check the current password."),
    DIFFERENT_NEW_PW(1010,HttpStatus.BAD_REQUEST,"New password and confirm password are different"),
    ROLE_EXIST(1011,HttpStatus.BAD_REQUEST,"Role is Exist"),
    ROLE_NOT_EXIST(1012,HttpStatus.BAD_REQUEST,"Role is not Exist"),
    EMAIL_OR_PASSWORD_NOT_MATCH(1013,HttpStatus.BAD_REQUEST,"Email or Password not match"),
    ACCESS_DENIED(1014,HttpStatus.FORBIDDEN,"Access Denied"),

    INTERNAL_ERROR(20000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    SPRING_INTERNAL_ERROR(20001, HttpStatus.INTERNAL_SERVER_ERROR, "Spring-detected internal error"),
    DATA_ACCESS_ERROR(20002, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),

    INVALID_DATA_ACCESS_REQUEST(40000, HttpStatus.BAD_REQUEST, "Invalid Data Access. Check your item again."),


    IS_NOT_JWT(50000, HttpStatus.UNAUTHORIZED, "Is not Jwt"),
    USER_INACTIVE(50001, HttpStatus.UNAUTHORIZED, "User inactive error"),
    USER_BAD_CREDENTIALS(50002,HttpStatus.UNAUTHORIZED,"User bad credentials"),
    TOKEN_NOT_VALID(50003,HttpStatus.UNAUTHORIZED,"Token not valid"),
    REFRESH_TOKEN_NOT_VALID(50004,HttpStatus.UNAUTHORIZED,"Refresh token not valid"),
    NOT_FOUND_SIGNATURE(50005,HttpStatus.UNAUTHORIZED,"Not found signature in Jwt")
    ;

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;


    public static ErrorCode valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) { throw new GeneralException("HttpStatus is null."); }

        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) { return ErrorCode.BAD_REQUEST; }
                    else if (httpStatus.is5xxServerError()) { return ErrorCode.INTERNAL_ERROR; }
                    else { return ErrorCode.OK; }
                });
    }

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }
}
