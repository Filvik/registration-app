package com.test_example.registration_app.controller;

import com.test_example.registration_app.service.LogoutService;
import com.test_example.registration_app.service.TokenBlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/logout")
@RequiredArgsConstructor
@Tag(name = "LogoutController", description = "Контроллер для выхода из системы.")
public class LogoutController {

    private final TokenBlacklistService tokenBlacklistService;
    private final LogoutService logoutService;

    @PostMapping
    @Operation(summary = "Выход пользователя", description = "Делает недействительным токен JWT для выхода пользователя из системы.")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") @Parameter(description = "Токен JWT для аннулирования") String token) {
        String jti = logoutService.extractJti(token);
        Long expirationTime = logoutService.extractExpirationTime(token);
        tokenBlacklistService.blacklistToken(jti, expirationTime - System.currentTimeMillis());

        log.info("User logged out and token invalidated");
        return ResponseEntity.ok().build();
    }
}

