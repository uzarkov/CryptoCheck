package com.example.cryptocheck.exception.general;

import com.example.cryptocheck.exception.RestException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends RestException {

    private static final String ERROR_MESSAGE = "Not enough permissions to perform this action";

    private UnauthorizedException(String errorMessage) {
        super(errorMessage, HttpStatus.FORBIDDEN);
    }

    public static UnauthorizedException unauthorized() {
        return new UnauthorizedException(ERROR_MESSAGE);
    }
}
