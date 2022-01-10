package com.example.pocketmark.security.provider;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VerifyResult {
    private boolean success;
    private String email;
}
