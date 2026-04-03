package com.sevenpay.merchantportal.user_management_service.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Resource not found");
    }
}
