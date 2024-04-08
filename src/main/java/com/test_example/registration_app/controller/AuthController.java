package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.JwtRequest;
import com.test_example.registration_app.dtos.RegistrationUserDto;
import com.test_example.registration_app.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "AuthController", description = "Контроллер для аутентификации и регистрации пользователей")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth")
    @Operation(summary = "Аутентификация пользователя",
            description = "Генерирует JWT токен для аутентифицированных пользователей")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    @PreAuthorize("hasAnyAuthority('Administrator')")
    @Operation(summary = "Регистрация нового пользователя",
            description = "Создает новую учетную запись пользователя")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }
}
