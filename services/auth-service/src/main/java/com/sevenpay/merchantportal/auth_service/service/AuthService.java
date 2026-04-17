package com.sevenpay.merchantportal.auth_service.service;

import com.sevenpay.merchantportal.auth_service.exception.*;
import com.sevenpay.merchantportal.auth_service.model.dto.*;
import com.sevenpay.merchantportal.auth_service.model.entity.BackupCode;
import com.sevenpay.merchantportal.auth_service.model.entity.PasswordReset;
import com.sevenpay.merchantportal.auth_service.model.entity.User;
import com.sevenpay.merchantportal.auth_service.model.entity.UserTwoFa;
import com.sevenpay.merchantportal.auth_service.model.enums.AccessLevel;
import com.sevenpay.merchantportal.auth_service.model.enums.Role;
import com.sevenpay.merchantportal.auth_service.model.enums.Status;
import com.sevenpay.merchantportal.auth_service.repository.BackupCodeRepository;
import com.sevenpay.merchantportal.auth_service.repository.PasswordResetRepository;
import com.sevenpay.merchantportal.auth_service.repository.UserRepository;
import com.sevenpay.merchantportal.auth_service.repository.UserTwoFaRepository;
import com.sevenpay.merchantportal.auth_service.util.JwtUtil;
import com.sevenpay.merchantportal.auth_service.util.TokenUtil;
import com.sevenpay.merchantportal.auth_service.util.TwoFaUtil;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final UserTwoFaRepository userTwoFaRepository;
    private final BackupCodeRepository backupCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final TwoFaUtil twoFaUtil;

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

        if (user.getPasswordHash() == null) {
            redisService.recordFailedAttempt(request.getEmail());
            throw new InvalidCredentialsException();
        }

        if (user.getStatus() == Status.DEACTIVATED) {
            throw new AccountDeactivatedException();
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            redisService.recordFailedAttempt(request.getEmail());
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

    public void resetPassword(ResetPasswordRequest request) {

        PasswordReset record = validateToken(request.getToken());
        applyPasswordChange(record.getUser(), request.getNewPassword(), record);

        applyPasswordChange(record.getUser(), request.getNewPassword(), record);
    }

    public void setPassword(SetPasswordRequest request) {

        PasswordReset record = validateToken(request.getToken());
        User user = record.getUser();

        if (!user.getFirstLoginFlag()) {
            throw new ForbiddenException("User not in first login state");
        }
        user.setFirstLoginFlag(false);
        applyPasswordChange(user, request.getNewPassword(), record);
    }

    public TwoFaSetupResponse setup2Fa(String accessToken) {
        JwtTokenValidationResponse validationResponse = validateJwtToken(accessToken);

        UUID userId = validationResponse.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        if (userTwoFaRepository.existsByUserId(userId)) {
            throw new ConflictException("2FA already enabled");
        }

        String secret = twoFaUtil.generateSecret();

        redisService.storeTwoFaSecret(userId, secret, 10);

        List<String> backupCodes = twoFaUtil.generateBackupCodes(8);

        List<String> hashed = backupCodes.stream()
                .map(twoFaUtil::hashBackupCode)
                .toList();

        redisService.storePendingBackupCodes(userId, hashed, 10);

        String qr = twoFaUtil.generateQrCodeUrl("SevenPay", user.getEmail() , secret);
        return TwoFaSetupResponse.builder()
                .secret(secret)
                .qrCodeUrl(qr)
                .backupCodes(backupCodes)
                .build();
    }

    @Transactional
    public void verifyTwoFaSetup(String accessToken, String code) {

        UUID userId = validateJwtToken(accessToken).getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (userTwoFaRepository.existsByUserId(userId)) {
            throw new ConflictException("2FA already enabled");
        }

        String secret = redisService.getTwoFaSecret(userId);

        if (secret == null) {
            throw new Invalid2FaTokenException();
        }

        boolean valid = twoFaUtil.verifyCode(secret, code);

        if (!valid) {
            throw new Invalid2FaTokenException();
        }

        List<String> hashedBackupCodes = redisService.getPendingBackupCodes(userId);

        if (hashedBackupCodes.isEmpty()) {
            throw new Invalid2FaTokenException();
        }

        UserTwoFa userTwoFa = new UserTwoFa();
        userTwoFa.setUser(user);
        userTwoFa.setSecret(secret);
        userTwoFaRepository.save(userTwoFa);

        List<BackupCode> backupCodes = hashedBackupCodes.stream()
                .map(hash -> {
                    BackupCode bc = new BackupCode();
                    bc.setUser(user);
                    bc.setCodeHash(hash);
                    return bc;
                })
                .toList();

        backupCodeRepository.saveAll(backupCodes);

        redisService.deleteTwoFaSecret(userId);
        redisService.deletePendingBackupCodes(userId);
    }

    private PasswordReset validateToken(String rawToken) {
        String hashedIncoming = TokenUtil.hashToken(rawToken);
        PasswordReset record = passwordResetRepository.findByResetToken(hashedIncoming)
                .orElseThrow(() -> new InvalidTokenException());
        if (record.getExpiry().isBefore(LocalDateTime.now()) || record.getUsedAt() != null) {
            throw new InvalidTokenException();
        }
        return record;
    }

    private void applyPasswordChange(User user, String newPassword, PasswordReset record) {
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        record.setUsedAt(LocalDateTime.now());
        passwordResetRepository.save(record);
    }

    public JwtTokenValidationResponse validateJwtToken(String jwtToken) {
        if (redisService.isTokenBlacklisted(jwtToken)) {
            throw new InvalidTokenException();
        }
        try {
            Claims claims = jwtUtil.extractClaims(jwtToken);
            return JwtTokenValidationResponse.builder()
                    .userId(UUID.fromString(claims.getSubject()))
                    .role(Role.valueOf(claims.get("role", String.class)))
                    .accessLevel(AccessLevel.valueOf(claims.get("accessLevel", String.class)))
                    .merchantId(claims.get("merchantId", String.class))
                    .build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new InvalidTokenException();
        }
    }
}
