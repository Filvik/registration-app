package com.test_example.registration_app.exeption_controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/noAuthorizationError")
public class UserAuthorizationError {

    @GetMapping
    @Operation(summary = "Показать страницу ошибки",
            description = "Отображение страницы ошибки с сообщением об ошибке. Доступен любым пользователям")
    public String showErrorPage(
            @Parameter(description = "Сообщение об ошибке для отображения на странице")
            @RequestParam(value = "error", required = false) String errorMessage,
            Model model) {
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
        }
        return "error";
    }
}
