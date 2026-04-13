package com.sevenpay.merchantportal.auth_service.controller;

import com.sevenpay.merchantportal.auth_service.model.dto.AuthResult;
import com.sevenpay.merchantportal.auth_service.model.dto.LoginRequest;
import com.sevenpay.merchantportal.auth_service.model.dto.LoginResponse;
import com.sevenpay.merchantportal.auth_service.model.dto.SetPasswordRequest;
import com.sevenpay.merchantportal.auth_service.service.AuthService;
import com.sevenpay.merchantportal.auth_service.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request,
                                               HttpServletResponse response) {
        AuthResult result = authService.login(request);
        response.addCookie(cookieUtil.createAccessTokenCookie(result.getAccessToken(), (int) result.getExpiresIn()));
        response.addCookie(cookieUtil.createRefreshTokenCookie(result.getRefreshToken()));
        return ResponseEntity.ok(LoginResponse.builder()
                .expiresIn(result.getExpiresIn())
                .firstLogin(result.isFirstLogin())
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            @CookieValue(name = "access_token") String accessToken,
            HttpServletResponse response) {

        authService.logout(accessToken);
        response.addCookie(cookieUtil.clearCookie("access_token"));
        response.addCookie(cookieUtil.clearCookie("refresh_token"));

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @PostMapping("/set-password")
    public ResponseEntity<Map<String, String>> setPassword(
            @RequestBody @Valid SetPasswordRequest request) {

        authService.setPassword(request);

        return ResponseEntity.ok(Map.of("message", "Password set successfully"));
    }
}
