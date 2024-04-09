package com.test_example.registration_app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@Tag(name = "EntranceController", description = "Контроллер для входа на универсальную страницу формы")
public class EntranceController {

    @GetMapping("/universalFormPage")
    @PreAuthorize("hasAnyAuthority('User', 'Operator', 'Administrator')")
    @Operation(summary = "Универсальная страница формы",
            description = "Предоставляет доступ к универсальной странице формы для пользователей с ролями User, Operator и Administrator",
            security = @SecurityRequirement(name = "bearerAuth"))
    public String adminAction(Model model, Authentication authentication) {
        try {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("authorities", authentication.getAuthorities());
            return "universalFormPage";
        }
        catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "error";
        }
    }
}
