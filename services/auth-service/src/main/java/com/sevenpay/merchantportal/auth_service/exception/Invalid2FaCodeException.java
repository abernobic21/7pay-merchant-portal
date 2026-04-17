package com.sevenpay.merchantportal.auth_service.exception;

public class Invalid2FaCodeException extends RuntimeException {
    public Invalid2FaCodeException() {
        super("Invalid code.");
    }
}
