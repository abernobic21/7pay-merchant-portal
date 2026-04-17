package com.sevenpay.merchantportal.auth_service.exception;

public class InvalidBackupCodeException extends RuntimeException {
    public InvalidBackupCodeException() {
        super("Invalid or already used backup code");
    }
}
