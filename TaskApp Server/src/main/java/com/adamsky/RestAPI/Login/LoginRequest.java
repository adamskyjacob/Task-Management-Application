package com.adamsky.RestAPI.Login;

import lombok.Data;

@Data
public class LoginRequest {
    private String identifier;
    private Boolean isEmail;
    private String password;

    public LoginRequest(String identifier,  Boolean isEmail, String password){
        this.identifier = identifier;
        this.isEmail = isEmail;
        this.password = password;
    }
}
