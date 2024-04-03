package com.test_example.registration_app.controller;

import com.test_example.registration_app.service.LogoutService;
import com.test_example.registration_app.service.TokenBlacklistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/logout")
@RequiredArgsConstructor
@Tag(name = "LogoutController", description = "Контроллер для выхода из системы")
public class LogoutController {

    private final TokenBlacklistService tokenBlacklistService;
    private final LogoutService logoutService;

    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        // Извлеките JTI и время истечения из токена
        String jti = logoutService.extractJti(token);
        Long expirationTime = logoutService.extractExpirationTime(token);
        // Инвалидируйте токен
        tokenBlacklistService.blacklistToken(jti, expirationTime - System.currentTimeMillis());

        return ResponseEntity.ok().build();
    }

}
