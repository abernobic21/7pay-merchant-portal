package com.sevenpay.merchantportal.auth_service.service;

import com.sevenpay.merchantportal.auth_service.exception.*;
import com.sevenpay.merchantportal.auth_service.model.dto.AuthResult;
import com.sevenpay.merchantportal.auth_service.model.dto.LoginRequest;
import com.sevenpay.merchantportal.auth_service.model.dto.SetPasswordRequest;
import com.sevenpay.merchantportal.auth_service.model.entity.PasswordReset;
import com.sevenpay.merchantportal.auth_service.model.entity.User;
import com.sevenpay.merchantportal.auth_service.model.enums.Status;
import com.sevenpay.merchantportal.auth_service.repository.PasswordResetRepository;
import com.sevenpay.merchantportal.auth_service.repository.UserRepository;
import com.sevenpay.merchantportal.auth_service.util.JwtUtil;
import com.sevenpay.merchantportal.auth_service.util.TokenUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    @Value("${jwt.access-expiry}")
    private long accessExpiry;

    @Value("${jwt.refresh-expiry}")
    private long refreshExpiry;

    public AuthResult login(LoginRequest request){
        if (redisService.isAccountLocked(request.getEmail())) {
            throw new AccountLockedException();
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    redisService.recordFailedAttempt(request.getEmail());
                    return new InvalidCredentialsException();
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            redisService.recordFailedAttempt(request.getEmail());
            throw new InvalidCredentialsException();
        }

        if (user.getStatus() == Status.DEACTIVATED) {
            throw new AccountDeactivatedException();
        }

        if (user.getPasswordHash() == null) {
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        redisService.storeRefreshToken(
                user.getId().toString(),
                refreshToken,
                refreshExpiry
        );

        redisService.clearFailedAttempts(request.getEmail());

        return AuthResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(accessExpiry)
                .firstLogin(user.getFirstLoginFlag())
                .build();
    }

    public void logout(String accessToken) {
            Claims claims = jwtUtil.extractClaims(accessToken);
            long remainingTtl = (claims.getExpiration().getTime() - System.currentTimeMillis()) / 1000;
            if (remainingTtl > 0) {
                redisService.blacklistToken(accessToken, remainingTtl);
            }
            String userId = claims.getSubject();
            redisService.deleteRefreshToken(userId);
    }

    public AuthResult refresh(String refreshToken) {
        Claims claims;
        try {
            claims = jwtUtil.extractClaims(refreshToken);
        } catch (Exception e) {
            throw new InvalidRefreshTokenException();
        }
            String userId = claims.getSubject();

            String storedToken = redisService.getRefreshToken(userId);
            if (storedToken == null || !storedToken.equals(refreshToken)) {
                throw new InvalidRefreshTokenException();
            }

            User user = userRepository.findById(UUID.fromString(userId))
                    .orElseThrow(() -> new InvalidRefreshTokenException());

            String newAccessToken = jwtUtil.generateAccessToken(user);
            String newRefreshToken = jwtUtil.generateRefreshToken(user);

            redisService.storeRefreshToken(userId, newRefreshToken, refreshExpiry);

            return AuthResult.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .expiresIn(accessExpiry)
                    .firstLogin(user.getFirstLoginFlag())
                    .build();
    }

    public void forgotPassword(String email){
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            String rawToken = TokenUtil.generateSecureToken();
            String hashedToken = TokenUtil.hashToken(rawToken);

            PasswordReset reset = PasswordReset.builder()
                    .user(user.get())
                    .resetToken(hashedToken)
                    .expiry(LocalDateTime.now().plusHours(1))
                    .build();

            passwordResetRepository.save(reset);

            emailService.sendForgotPasswordEmail(email, rawToken);
        }
    }

    public void setPassword(SetPasswordRequest request) {

        String hashedIncoming = TokenUtil.hashToken(request.getToken());

        PasswordReset record = passwordResetRepository.findByResetToken(hashedIncoming)
                .orElseThrow(() -> new InvalidTokenException());

        if (record.getExpiry().isBefore(LocalDateTime.now()) || record.getUsedAt() != null) {
            throw new InvalidTokenException();
        }

        User user = record.getUser();

        if(!user.getFirstLoginFlag()) {
            throw new ForbiddenException("User not in first login state");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setFirstLoginFlag(false);
        userRepository.save(user);

        record.setUsedAt(LocalDateTime.now());
        passwordResetRepository.save(record);
    }
}
