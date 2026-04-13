package com.sevenpay.merchantportal.auth_service.controller;

import com.sevenpay.merchantportal.auth_service.model.dto.SetPasswordRequest;
import com.sevenpay.merchantportal.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/set-password")
    public ResponseEntity<Map<String, String>> setPassword(
            @RequestBody @Valid SetPasswordRequest request) {

        authService.setPassword(request);

        return ResponseEntity.ok(Map.of("message", "Password set successfully"));
    }
}
