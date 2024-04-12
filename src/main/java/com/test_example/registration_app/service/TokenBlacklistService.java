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

    /**
     * Добавляет токен в черный список на заданное время.
     *
     * @param jti Идентификатор JWT (JWT ID), который нужно добавить в черный список.
     * @param expireDuration Время в миллисекундах, после которого запись должна быть удалена из Redis.
     */
    public void blacklistToken(String jti, long expireDuration) {
        try {
            redisTemplate.opsForValue().set(jti, "blacklisted", expireDuration, TimeUnit.MILLISECONDS);
            log.info("Token with JTI: {} has been blacklisted for {} milliseconds", jti, expireDuration);
        } catch (Exception e) {
            log.error("Failed to blacklist token with JTI: {}. Error: {}", jti, e.getMessage());
            throw new RuntimeException("Error blacklisting token", e);
        }
    }

    /**
     * Проверяет, находится ли токен в черном списке.
     *
     * @param jti Идентификатор JWT (JWT ID), который проверяется на наличие в черном списке.
     * @return true, если токен находится в черном списке, иначе false.
     */
    public boolean isTokenBlacklisted(String jti) {
        try {
            boolean isBlacklisted = Boolean.TRUE.equals(redisTemplate.hasKey(jti));
            log.info("Token with JTI: {} is {} in blacklist", jti, isBlacklisted ? "currently" : "not");
            return isBlacklisted;
        } catch (Exception e) {
            log.error("Failed to check if token with JTI: {} is blacklisted. Error: {}", jti, e.getMessage());
            throw new RuntimeException("Error checking token blacklist status", e);
        }
    }
}
