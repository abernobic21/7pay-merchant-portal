package com.sevenpay.merchantportal.auth_service.exception;

public class Invalid2FaSetupCodeException extends RuntimeException {
    public Invalid2FaSetupCodeException() {
        super("Invalid or expired code.");
    }
}
