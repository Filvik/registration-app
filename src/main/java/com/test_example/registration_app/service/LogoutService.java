package com.test_example.registration_app.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String extractJti(String token) {
        // Предполагается, что токен поступает с префиксом 'Bearer ', который нужно удалить
        token = token.replace("Bearer ", "");
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return claimsJws.getBody().getId(); // Получаем JTI
    }

    public Long extractExpirationTime(String token) {
        token = token.replace("Bearer ", "");
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return claimsJws.getBody().getExpiration().getTime();
    }
}
