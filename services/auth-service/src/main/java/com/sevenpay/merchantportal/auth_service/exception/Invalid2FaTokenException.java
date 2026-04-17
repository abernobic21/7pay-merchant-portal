package com.sevenpay.merchantportal.auth_service.exception;

public class Invalid2FaTokenException extends RuntimeException {
    public Invalid2FaTokenException() {
        super("Invalid or expired code.");
    }
}
