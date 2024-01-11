package com.adamsky.RestAPI;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginSuccess extends CredentialResponse {
    String token;

    public LoginSuccess(String token){
        this.token = token;
    }

    @Override
    public String toString(){
        return this.token;
    }
}
