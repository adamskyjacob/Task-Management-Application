package com.adamsky.RestAPI.Register;

import com.adamsky.RestAPI.Credential.CredentialResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterSuccess extends CredentialResponse {
    private String token;
    private Long id;

    public RegisterSuccess(String token, Long id){
        this.id = id;
        this.token = token;
    }

    @Override
    public String toString(){
        return this.token;
    }
}
