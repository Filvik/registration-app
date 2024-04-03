package com.test_example.registration_app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "LoginController", description = "Контроллер для управления процессом входа в систему")
public class LoginController {

    @GetMapping("/login")
    @Operation(summary = "Страница входа", description = "Отображает страницу для входа в систему")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    @Operation(summary = "Ошибка входа", description = "Отображает страницу входа с сообщением об ошибке при неверных учетных данных")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
