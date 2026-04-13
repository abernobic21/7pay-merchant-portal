package com.sevenpay.merchantportal.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
}
