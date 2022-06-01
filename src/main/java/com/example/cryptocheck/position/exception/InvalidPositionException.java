package com.example.cryptocheck.position.exception;

import com.example.cryptocheck.exception.RestException;
import org.springframework.http.HttpStatus;

public class InvalidPositionException extends RestException {

    private static final String ERROR_MESSAGE = "Invalid position";

    private InvalidPositionException(String errorMessage) {
        super(errorMessage, HttpStatus.CONFLICT);
    }

    public static InvalidPositionException invalidPosition() {
        return new InvalidPositionException(ERROR_MESSAGE);
    }
}
