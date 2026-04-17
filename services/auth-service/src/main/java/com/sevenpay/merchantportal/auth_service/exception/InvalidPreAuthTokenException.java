package com.sevenpay.merchantportal.auth_service.exception;

public class InvalidPreAuthTokenException extends RuntimeException {
    public InvalidPreAuthTokenException() {
        super("Invalid or expired pre-auth token");
    }
}
