package com.adamsky.RestAPI.TokenValidation;

import lombok.Data;

@Data
public class TokenValidationRequest {
    private String token;
    private String identifier;

    public TokenValidationRequest(String token, String identifier){
        this.token = token;
        this.identifier = identifier;
    }
}
