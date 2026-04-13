package com.sevenpay.merchantportal.auth_service.exception;

public class AccountLockedException extends RuntimeException {
    public AccountLockedException() {
        super("Account locked");
    }
}
