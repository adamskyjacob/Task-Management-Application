package com.adamsky.RestAPI.Register;

import lombok.Data;

@Data
public class RegisterRequest {
    private String identifier;
    private Boolean isEmail;
    private String password;

    public RegisterRequest(String identifier,  Boolean isEmail, String password){
        this.identifier = identifier;
        this.isEmail = isEmail;
        this.password = password;
    }
}
