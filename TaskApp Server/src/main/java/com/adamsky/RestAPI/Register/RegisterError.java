package com.adamsky.RestAPI.Register;

import com.adamsky.RestAPI.Credential.CredentialResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterError extends CredentialResponse {
    private String error;

    public RegisterError(String error) {
        this.error = error;
    }

    public static RegisterError from(HttpStatus status){
        switch(status){
            case CONFLICT -> {
                return new RegisterError("User not found.");
            }
            case INTERNAL_SERVER_ERROR -> {
                return new RegisterError("Internal server error.");
            }
            default -> {
                return new RegisterError("Unhandled error.");
            }
        }
    }
}
