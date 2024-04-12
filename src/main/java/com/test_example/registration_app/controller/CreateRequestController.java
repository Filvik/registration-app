package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.service.CreateRequestService;
import com.test_example.registration_app.service.RequestConverterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CreateRequestController", description = "Контроллер для создания заявок")
public class CreateRequestController {

    private final CreateRequestService createRequestService;
    private final RequestConverterService requestConverterService;

    @GetMapping("/createRequestForm")
    @PreAuthorize("hasAnyAuthority('User')")
    @Operation(summary = "Форма создания заявки",
            description = "Отображает форму для создания новой заявки пользователем. Доступен только юзерам.")
    public String showCreateRequestForm() {
        return "create_request_form";
    }

    @PostMapping("/createRequest")
    @PreAuthorize("hasAnyAuthority('User')")
    @Operation(summary = "Создание заявки",
            description = "Создает новую заявку на основе предоставленных данных. Доступен только юзерам.")
    public String createRequest(@Parameter(description = "DTO новой заявки")
//                              @ModelAttribute RequestDto requestDto,   // для использование в браузуре
                                @RequestBody RequestDto requestDto,   // для использование в swagger
                                Model model,
                                Authentication authentication) {
        if (authentication.getName().equals(requestDto.getUserName())) {
            try {
                Request request = createRequestService.createRequest(requestDto);
                requestDto = requestConverterService.fromRequestToRequestDto(request);
                model.addAttribute("requestDto", requestDto);
                return "request_was_successfully_created";
            } catch (Exception e) {
                log.warn("Error: " + e.getMessage());
                model.addAttribute("errorMessage", "Error: " + e.getMessage());
                return "error";
            }
        } else {
            log.warn("Error user ");
            model.addAttribute("errorMessage", "The name in the authentication does not match the name in the application.");
            return "error";
        }
    }
}
