package com.example.cryptocheck.cryptocurrency;

import com.example.cryptocheck.exception.RestException;
import org.springframework.http.HttpStatus;

public class NoSuchCryptocurrencyException extends RestException {

    private static final String ERROR_MESSAGE = "There is no such position";

    private NoSuchCryptocurrencyException(String errorMessage) {
        super(errorMessage, HttpStatus.NOT_FOUND);
    }

    public static NoSuchCryptocurrencyException invalid() {
        return new NoSuchCryptocurrencyException(ERROR_MESSAGE);
    }
}

