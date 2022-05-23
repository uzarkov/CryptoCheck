package com.example.cryptocheck.api.price;

import com.example.cryptocheck.exception.RestException;
import org.springframework.http.HttpStatus;

public class PriceServiceException extends RestException {
    private PriceServiceException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public static PriceServiceException unsupportedSymbolException(String symbol) {
        return new PriceServiceException("Symbol <%s> is not supported.".formatted(symbol));
    }

    public static PriceServiceException unsupportedIntervalException(String intervalId) {
        return new PriceServiceException("Interval <%s> is not supported.".formatted(intervalId));
    }
}
