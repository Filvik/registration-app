package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.service.RequestDtoConverterService;
import com.test_example.registration_app.service.UpdateRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/api")
@Tag(name = "CreatedRequestViewController", description = "Контроллер для работы с созданными заявками")
public class CreatedRequestViewController {

    private final RequestDtoConverterService requestDtoConverterService;
    private final UpdateRequestService updateRequestService;

    @GetMapping("/created-request/{id}")
    @PreAuthorize("hasAnyAuthority('Operator')")
    @Operation(summary = "Получение заявки по ID",
            description = "Позволяет оператору получить детали заявки по её ID",
            responses = {
                    @ApiResponse(description = "Успешное получение деталей заявки",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = RequestDto.class))),
                    @ApiResponse(description = "Ошибка доступа или не найдена заявка", responseCode = "403")
            })
    public String getRequestById(@Parameter(description = "ID заявки") @PathVariable Long id, Model model, Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            Request requestFromDb = updateRequestService.getRequestById(id);
            if (updateRequestService.checkName(requestFromDb.getUser().getFullName(), authentication.getName())) {
                RequestDto request = requestDtoConverterService.fromRequestToRequestDto(requestFromDb);
                model.addAttribute("request", request);
                return "request_details";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Not available for viewing");
                return "redirect:/error";
            }
        } catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/error";
        }
    }
}
