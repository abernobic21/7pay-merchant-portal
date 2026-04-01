package com.sevenpay.merchantportal.core_service.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super("Validation error: " + message);
    }
}
