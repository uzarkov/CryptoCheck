package com.example.cryptocheck.portfolio.exception;

import com.example.cryptocheck.exception.RestException;
import org.springframework.http.HttpStatus;

public class NegativeTotalQuantityException extends RestException {

    private static final String ERROR_MESSAGE = "Total quantity can not be negative";

    private NegativeTotalQuantityException(String errorMessage) {
        super(errorMessage, HttpStatus.BAD_REQUEST);
    }

    public static NegativeTotalQuantityException invalidQuantity() {
        return new NegativeTotalQuantityException(ERROR_MESSAGE);
    }
}
