package com.sevenpay.merchantportal.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public void blacklistToken(String token, long ttlSeconds) {
        redisTemplate.opsForValue().set(
                blacklistKey(token),
                "blacklisted",
                ttlSeconds,
                TimeUnit.SECONDS
        );
    }

    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey(token)));
    }


    public void recordFailedAttempt(String email) {
        String key = lockoutKey(email);
        Long attempts = redisTemplate.opsForValue().increment(key);
        if (attempts == 1) {
            redisTemplate.expire(key, 15, TimeUnit.MINUTES);
        }
    }

    public boolean isAccountLocked(String email) {
        String value = redisTemplate.opsForValue().get(lockoutKey(email));
        return value != null && Integer.parseInt(value) >= 5;
    }

    public void clearFailedAttempts(String email) {
        redisTemplate.delete(lockoutKey(email));
    }

    public void storeRefreshToken(String userId, String token, long ttlSeconds) {
        redisTemplate.opsForValue().set(
                refreshKey(userId),
                token,
                ttlSeconds,
                TimeUnit.SECONDS
        );
    }

    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get(refreshKey(userId));
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(refreshKey(userId));
    }


    private String blacklistKey(String token) {
        return "blacklist:" + token;
    }

    private String lockoutKey(String email) {
        return "lockout:" + email;
    }

    private String refreshKey(String userId) {
        return "refresh:" + userId;
    }

    private static final String TWO_FA_SETUP_PREFIX = "2fa:setup:";
    private static final String TWO_FA_BACKUP_PREFIX = "2fa:setup:backup:";

    public void storeTwoFaSecret(UUID userId, String secret, long ttlMinutes) {
        redisTemplate.opsForValue().set(
                TWO_FA_SETUP_PREFIX + userId,
                secret,
                ttlMinutes,
                TimeUnit.MINUTES
        );
    }

    public String getTwoFaSecret(UUID userId) {
        return redisTemplate.opsForValue().get(TWO_FA_SETUP_PREFIX + userId);
    }

    public void deleteTwoFaSecret(UUID userId) {
        redisTemplate.delete(TWO_FA_SETUP_PREFIX + userId);
    }

    public void storePendingBackupCodes(UUID userId, List<String> hashedCodes, long ttlMinutes) {
        redisTemplate.opsForValue().set(
                TWO_FA_BACKUP_PREFIX + userId,
                String.join(",", hashedCodes),
                ttlMinutes,
                TimeUnit.MINUTES
        );
    }

    public List<String> getPendingBackupCodes(UUID userId) {
        String value = redisTemplate.opsForValue().get(TWO_FA_BACKUP_PREFIX + userId);

        if (value == null) return Collections.emptyList();

        return Arrays.asList(value.split(","));
    }

    public void deletePendingBackupCodes(UUID userId) {
        redisTemplate.delete(TWO_FA_BACKUP_PREFIX + userId);
    }
}
