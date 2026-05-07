package com.sevenpay.merchantportal.user_management_service.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void sendSetPasswordEmail(String to, String token) {

        String link = "http://localhost:8080/set-password?token=" + token;

        System.out.println("SET PASSWORD EMAIL");
        System.out.println("To: " + to);
        System.out.println("Link: " + link);
    }
}
