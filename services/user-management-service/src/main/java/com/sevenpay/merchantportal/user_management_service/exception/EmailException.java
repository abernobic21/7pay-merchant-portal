package com.sevenpay.merchantportal.user_management_service.exception;

public class EmailException extends RuntimeException {
    public EmailException() {
        super("Email sending failed");
    }
}
