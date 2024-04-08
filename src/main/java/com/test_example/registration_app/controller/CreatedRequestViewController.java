package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.service.RequestConverterService;
import com.test_example.registration_app.service.UpdateRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.test_example.registration_app.enums.EnumStatus.DRAFT;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/api")
@Tag(name = "CreatedRequestViewController", description = "Контроллер для работы с созданными заявками")
public class CreatedRequestViewController {

    private final RequestConverterService requestConverterService;
    private final UpdateRequestService updateRequestService;

    @GetMapping("/createdRequest/{id}")
    @PreAuthorize("hasAnyAuthority('Operator')")
    @Operation(summary = "Получение заявки по ID",
            description = "Позволяет оператору получить детали заявки по её ID")
    public String getRequestById(@Parameter(description = "ID заявки") @PathVariable Long id,
                                 Model model,
                                 Authentication authentication) {
        try {
            Request requestFromDb = updateRequestService.getRequestById(id);
            if (!requestFromDb.getStatus().equals(DRAFT)) {
                RequestDto request = requestConverterService.fromRequestToRequestDto(requestFromDb);
                model.addAttribute("request", request);
                return "request_details";
            } else {
                log.warn("Error status: " + requestFromDb.getStatus());
                model.addAttribute("errorMessage", "Error status: " + requestFromDb.getStatus());
                return "error";
            }
        } catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "error";
        }
    }
}
