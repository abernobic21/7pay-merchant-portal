package com.sevenpay.merchantportal.auth_service.exception;

public class AccountDeactivatedException extends RuntimeException {
    public AccountDeactivatedException() {
        super("Account is deactivated");
    }
}
