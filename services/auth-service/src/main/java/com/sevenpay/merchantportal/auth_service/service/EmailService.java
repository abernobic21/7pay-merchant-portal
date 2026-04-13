package com.sevenpay.merchantportal.auth_service.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void sendForgotPasswordEmail(String to, String token) {

        String link = "http://localhost:8080/api/v1/auth/reset-password?token=" + token;

        System.out.println("RESET PASSWORD EMAIL");
        System.out.println("To: " + to);
        System.out.println("Link: " + link);
    }
}
