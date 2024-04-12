package com.test_example.registration_app.utils;

import com.test_example.registration_app.service.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenUtils {

    private final TokenBlacklistService tokenBlacklistService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    /**
     * Генерирует JWT для пользователя.
     * @param userDetails данные пользователя.
     * @return сгенерированный JWT.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", rolesList);

        String jti = UUID.randomUUID().toString();
        claims.put("jti", jti);

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * Извлекает имя пользователя из JWT.
     * @param token JWT.
     * @return имя пользователя.
     */
    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    /**
     * Извлекает роли пользователя из JWT.
     * @param token JWT.
     * @return список ролей пользователя.
     */
    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }

    /**
     * Возвращает все утверждения (claims) из JWT.
     * @param token JWT.
     * @return утверждения.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Проверяет валидность JWT.
     * @param token JWT.
     * @return true, если токен действителен и не находится в черном списке.
     */
    public boolean validateToken(String token) {
        try {
            final Claims claims = getAllClaimsFromToken(token);
            final String jti = claims.getId();
            boolean isTokenValid = !tokenBlacklistService.isTokenBlacklisted(jti) && !claims.getExpiration().before(new Date());
            if (!isTokenValid) {
                log.info("Token with JTI: {} is invalid or blacklisted", jti);
            }
            return isTokenValid;
        } catch (Exception e) {
            log.error("Token validation error: {}", e.getMessage());
            return false;
        }
    }
}
