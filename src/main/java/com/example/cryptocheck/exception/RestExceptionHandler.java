package com.example.cryptocheck.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String FALLBACK_MESSAGE = "Something went wrong";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestExceptionResponse> handleException() {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(status)
                .body(new RestExceptionResponse(FALLBACK_MESSAGE, status.value(), status.getReasonPhrase()));
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<RestExceptionResponse> handleRestException(RestException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(RestExceptionResponse.from(exception));
    }
}