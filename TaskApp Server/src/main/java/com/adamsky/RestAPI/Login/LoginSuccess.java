package com.adamsky.RestAPI.Login;

import com.adamsky.RestAPI.Credential.CredentialResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginSuccess extends CredentialResponse {
    private String token;
    private Long id;

    public LoginSuccess(String token, Long id){
        this.token = token;
        this.id = id;
    }

    @Override
    public String toString(){
        return this.token;
    }
}
