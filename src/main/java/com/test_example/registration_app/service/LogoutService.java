package com.test_example.registration_app.service;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LogoutService {

    @Value("${jwt.secret}")
    private String secretKey;
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Извлекает JTI (JWT ID) из токена.
     *
     * @param token JWT токен в формате строки.
     * @return JTI из токена.
     * @throws ResponseStatusException с соответствующим HTTP статусом, если токен истек, некорректен или при обработке возникла ошибка.
     */
    public String extractJti(String token) {
        try {
            token = token.replace("Bearer ", "");
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return claimsJws.getBody().getId(); // Получаем JTI
        } catch (ExpiredJwtException e) {
            log.error("Token expired: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
        } catch (MalformedJwtException | SignatureException e) {
            log.error("Token invalid: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
        } catch (Exception e) {
            log.error("Error extracting JTI: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing token");
        }
    }

    /**
     * Извлекает время истечения токена.
     *
     * @param token JWT токен в формате строки.
     * @return время истечения токена в миллисекундах.
     * @throws ResponseStatusException с соответствующим HTTP статусом, если токен истек, некорректен или при обработке возникла ошибка.
     */
    public Long extractExpirationTime(String token) {
        try {
            token = token.replace("Bearer ", "");
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return claimsJws.getBody().getExpiration().getTime();
        } catch (ExpiredJwtException e) {
            log.error("Token expired: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
        } catch (MalformedJwtException | SignatureException e) {
            log.error("Token invalid: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
        } catch (Exception e) {
            log.error("Error extracting expiration time: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing token");
        }
    }

    /**
     * Возвращает оставшееся время жизни ключа в Redis.
     *
     * @param key ключ в Redis.
     * @return время в секундах до истечения срока действия ключа.
     */
    public Long getKeyExpiration(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
}
