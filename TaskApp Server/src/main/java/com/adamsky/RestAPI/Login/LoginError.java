package com.adamsky.RestAPI.Login;

import com.adamsky.RestAPI.CredentialResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginError extends CredentialResponse {
    String errorMessage;
    public LoginError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static LoginError from(HttpStatus status){
        switch(status){
            case NOT_FOUND -> {
                return new LoginError("User not found.");
            }
            case INTERNAL_SERVER_ERROR -> {
                return new LoginError("Error validating login.");
            }
            case FORBIDDEN -> {
                return new LoginError("Incorrect password.");
            }
            default -> {
                return new LoginError("Unhandled error.");
            }
        }
    }

    @Override
    public String toString(){
        return this.errorMessage;
    }
}
