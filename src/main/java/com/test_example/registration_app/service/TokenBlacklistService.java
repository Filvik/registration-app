package com.test_example.registration_app.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    public void blacklistToken(String jti, long expireDuration) {
        redisTemplate.opsForValue().set(jti, "blacklisted", expireDuration, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenBlacklisted(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(jti));
    }
}
