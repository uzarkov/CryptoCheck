package com.example.cryptocheck.position.exception;

import com.example.cryptocheck.exception.RestException;
import org.springframework.http.HttpStatus;

public class NoSuchPositionException extends RestException {

    private static final String ERROR_MESSAGE = "There is no such position";

    private NoSuchPositionException(String errorMessage) {
        super(errorMessage, HttpStatus.NOT_FOUND);
    }

    public static NoSuchPositionException invalidId() {
        return new NoSuchPositionException(ERROR_MESSAGE);
    }
}
