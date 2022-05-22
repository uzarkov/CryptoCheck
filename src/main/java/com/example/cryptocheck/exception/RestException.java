package com.example.cryptocheck.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class RestException extends RuntimeException {
    private final HttpStatus status;

    public RestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public RestException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }
}