package com.example.pocketmark.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ValidError {

    private String field;
    private String message;
    private String invalidValue;

}
