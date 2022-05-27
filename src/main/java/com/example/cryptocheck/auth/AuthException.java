package com.example.cryptocheck.auth;

import com.example.cryptocheck.auth.oauth.OAuthProvider;
import com.example.cryptocheck.exception.RestException;
import org.springframework.http.HttpStatus;

public class AuthException extends RestException {
    private AuthException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public static AuthException invalidCredentials() {
        return new AuthException("Invalid credentials.");
    }

    public static AuthException invalidProvider(OAuthProvider requiredProvider) {
        var msg = "You're already signed up with %s account. Please use your %s account to sign in";
        var friendlyName = requiredProvider.getFriendlyName();
        return new AuthException(msg.formatted(friendlyName, friendlyName));
    }
}
