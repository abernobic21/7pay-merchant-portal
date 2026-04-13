package com.sevenpay.merchantportal.auth_service.controller;

import com.sevenpay.merchantportal.auth_service.exception.InvalidRefreshTokenException;
import com.sevenpay.merchantportal.auth_service.model.dto.*;
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

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response) {

        if (refreshToken == null) {
            throw new InvalidRefreshTokenException();
        }

        AuthResult result = authService.refresh(refreshToken);

        response.addCookie(cookieUtil.createAccessTokenCookie(result.getAccessToken(), (int) result.getExpiresIn()));
        response.addCookie(cookieUtil.createRefreshTokenCookie(result.getRefreshToken()));

        return ResponseEntity.ok(RefreshTokenResponse.builder()
                .expiresIn(result.getExpiresIn())
                .build());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @RequestBody @Valid ForgotPasswordRequest request) {

        authService.forgotPassword(request.getEmail());

        return ResponseEntity.ok(Map.of("message", "If the email exists, a reset link has been sent"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @RequestBody @Valid ResetPasswordRequest request) {

        authService.resetPassword(request);

        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }

    @PostMapping("/set-password")
    public ResponseEntity<Map<String, String>> setPassword(
            @RequestBody @Valid SetPasswordRequest request) {

        authService.setPassword(request);

        return ResponseEntity.ok(Map.of("message", "Password set successfully"));
    }
}
