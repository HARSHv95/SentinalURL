package com.harsh.sentinal.scan.util;

public class SsrfViolationException extends RuntimeException {
    public SsrfViolationException(String message) {
        super(message);
    }
}
