package com.adamsky.RestAPI.TokenValidation;

import lombok.Data;

@Data
public class TokenValidationRequest {
    private String token;
    private String identifier;
}
