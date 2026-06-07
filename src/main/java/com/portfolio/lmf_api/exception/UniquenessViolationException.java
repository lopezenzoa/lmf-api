package com.portfolio.lmf_api.exception;

public class UniquenessViolationException extends RuntimeException {
    public UniquenessViolationException(String message) {
        super(message);
    }
}
