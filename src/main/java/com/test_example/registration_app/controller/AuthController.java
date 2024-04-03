package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.JwtRequest;
import com.test_example.registration_app.dtos.RegistrationUserDto;
import com.test_example.registration_app.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
            description = "Генерирует JWT токен для аутентифицированных пользователей",
            responses = {
                    @ApiResponse(description = "Успешная аутентификация",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = JwtRequest.class))),
                    @ApiResponse(description = "Ошибка аутентификации", responseCode = "401")
            })
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    @Operation(summary = "Регистрация нового пользователя",
            description = "Создает новую учетную запись пользователя",
            responses = {
                    @ApiResponse(description = "Успешная регистрация",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = RegistrationUserDto.class))),
                    @ApiResponse(description = "Ошибка регистрации", responseCode = "400")
            })
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }
}
