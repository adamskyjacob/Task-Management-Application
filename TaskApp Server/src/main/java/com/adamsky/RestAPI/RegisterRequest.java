package com.adamsky.RestAPI;

import lombok.Data;

@Data
public class RegisterRequest {
    public String identifier;
    public Boolean isEmail;
    public String password;

    public RegisterRequest(String identifier,  Boolean isEmail, String password){
        this.identifier = identifier;
        this.isEmail = isEmail;
        this.password = password;
    }
}
