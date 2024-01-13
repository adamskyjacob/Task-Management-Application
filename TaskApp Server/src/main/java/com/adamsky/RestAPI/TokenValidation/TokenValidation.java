package com.adamsky.RestAPI.TokenValidation;

import lombok.Data;

@Data
public class TokenValidation {
    private boolean valid;

    public TokenValidation(boolean valid){
        this.valid = valid;
    }
}
