package com.portfolio.lmf_api.exception;

public class InvalidRequestFieldException extends RuntimeException {
    public InvalidRequestFieldException(String message) {
        super(message);
    }
}
