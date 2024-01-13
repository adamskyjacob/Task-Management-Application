package com.adamsky.RestAPI.Register;

import com.adamsky.RestAPI.CredentialResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterSuccess extends CredentialResponse {
    String token;

    public RegisterSuccess(String token){
        this.token = token;
    }

    @Override
    public String toString(){
        return this.token;
    }
}
