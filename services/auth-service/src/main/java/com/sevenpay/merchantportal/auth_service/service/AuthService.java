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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
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

    public AuthResult login(LoginRequest request) {
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
        redisService.clearFailedAttempts(request.getEmail());

        if (userTwoFaRepository.existsByUserId(user.getId())) {
            return buildPreAuthResult(user);
        }

        return buildFullAuthResult(user);
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

        return buildFullAuthResult(user);
    }

    public void forgotPassword(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            String rawToken = TokenUtil.generateSecureToken();

            PasswordReset reset = PasswordReset.builder()
                    .user(user)
                    .resetToken(TokenUtil.hashToken(rawToken))
                    .expiry(LocalDateTime.now().plusHours(1))
                    .build();

            passwordResetRepository.save(reset);
            emailService.sendForgotPasswordEmail(email, rawToken);
        });
    }

    public void resetPassword(ResetPasswordRequest request) {

        PasswordReset record = validateResetToken(request.getToken());
        applyPasswordChange(record.getUser(), request.getNewPassword(), record);
    }

    public void setPassword(SetPasswordRequest request) {

        PasswordReset record = validateResetToken(request.getToken());
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
        List<String> backupCodes = twoFaUtil.generateBackupCodes(8);
        List<String> hashed = backupCodes.stream()
                .map(twoFaUtil::hashBackupCode)
                .toList();

        redisService.storeTwoFaSecret(userId, secret, 10);
        redisService.storePendingBackupCodes(userId, hashed, 10);

        return TwoFaSetupResponse.builder()
                .secret(secret)
                .qrCodeUrl(twoFaUtil.generateQrCodeUrl("SevenPay", user.getEmail(), secret))
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
            throw new Invalid2FaSetupCodeException();
        }


        if (!twoFaUtil.verifyCode(secret, code)) {
            throw new Invalid2FaSetupCodeException();
        }

        List<String> hashedBackupCodes = redisService.getPendingBackupCodes(userId);
        if (hashedBackupCodes.isEmpty()) {
            throw new Invalid2FaSetupCodeException();
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

    @Transactional
    public AuthResult validateTwoFa(@Valid TwoFaValidationRequest request) {
        User user = consumePreAuthToken(request.getPreAuthToken());

        UserTwoFa twoFa = userTwoFaRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ForbiddenException("2FA not enabled"));

        if (redisService.isTotpUsed(user.getId(), request.getCode())) {

            throw new Invalid2FaCodeException();
        }

        if (!twoFaUtil.verifyCode(twoFa.getSecret(), request.getCode())) {
            throw new Invalid2FaCodeException();
        }

        redisService.markTotpUsed(user.getId(), request.getCode());

        return buildFullAuthResult(user);
    }

    @Transactional
    public AuthResult validateTwoFaBackupCode(TwoFaBackupRequest request) {
        User user = consumePreAuthToken(request.getPreAuthToken());

        List<BackupCode> unusedCodes = backupCodeRepository.findByUserIdAndUsedAtIsNull(user.getId());

        BackupCode matched = unusedCodes.stream()
                .filter(bc -> passwordEncoder.matches(request.getBackupCode(), bc.getCodeHash()))
                .findFirst()
                .orElseThrow(InvalidBackupCodeException::new);

        matched.setUsedAt(Instant.now());
        backupCodeRepository.save(matched);

        return buildFullAuthResult(user);
    }

    private PasswordReset validateResetToken(String rawToken) {
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

    private AuthResult buildFullAuthResult(User user) {
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        redisService.storeRefreshToken(user.getId().toString(), refreshToken, refreshExpiry);

        return AuthResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(accessExpiry)
                .firstLogin(user.getFirstLoginFlag())
                .build();
    }

    private AuthResult buildPreAuthResult(User user) {
        String preAuthToken = TokenUtil.generateSecureToken();
        redisService.storePreAuthToken(user.getId(), preAuthToken, 5);

        return AuthResult.builder()
                .requires2fa(true)
                .preAuthToken(preAuthToken)
                .expiresIn(0)
                .firstLogin(user.getFirstLoginFlag())
                .build();
    }

    private User consumePreAuthToken(String token) {
        UUID userId = redisService.consumePreAuthToken(token);
        if (userId == null) {
            throw new InvalidPreAuthTokenException();
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
