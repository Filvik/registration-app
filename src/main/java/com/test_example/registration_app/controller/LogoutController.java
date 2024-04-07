package com.test_example.registration_app.controller;

import com.test_example.registration_app.service.LogoutService;
import com.test_example.registration_app.service.TokenBlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Slf4j
@Controller
@RequestMapping("/logout")
@RequiredArgsConstructor
@Tag(name = "LogoutController", description = "Контроллер для выхода из системы.")
public class LogoutController {

    private final TokenBlacklistService tokenBlacklistService;
    private final LogoutService logoutService;

    @PostMapping
    @Operation(summary = "Выход пользователя", description = "Отображает страницу выхода и делает недействительным токен JWT.")
    public String logout(
            @RequestHeader("Authorization") String token,
            Model model) {
        try {
            String jti = logoutService.extractJti(token);
            Long expirationTime = logoutService.extractExpirationTime(token);
            tokenBlacklistService.blacklistToken(jti, expirationTime - System.currentTimeMillis());
            log.info("User logged out and token invalidated");
            model.addAttribute("logoutMessage", "Вы успешно вышли из системы.");
        } catch (Exception e) {
            log.error("Ошибка при выходе: ", e);
            model.addAttribute("logoutError", "Произошла ошибка при попытке выхода.");
            return "error";
        }
        return "logout";
    }
}