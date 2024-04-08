package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.enums.EnumStatus;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.service.RequestConverterService;
import com.test_example.registration_app.service.UpdateRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@Controller
@RequestMapping("/update")
@RequiredArgsConstructor
@Tag(name = "UpdateRequestController", description = "Контроллер для обновления заявок")
public class UpdateRequestController {

    private final UpdateRequestService updateRequestService;
    private final RequestConverterService requestConverterService;

    @GetMapping("/edit")
    @PreAuthorize("hasAnyAuthority('User')")
    @Operation(summary = "Показать форму редактирования заявки", description = "Показывает форму для редактирования заявки пользователем")
    public String showEditForm(@Parameter(description = "ID заявки для редактирования") @RequestParam Long idRequest,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            Request request = updateRequestService.getRequestById(idRequest);
            if (!request.getStatus().equals(EnumStatus.DRAFT)){
                return "redirect:/api/createdRequest/" + request.getId();
            }
            RequestDto requestDto = requestConverterService.fromRequestToRequestDto(request);
            model.addAttribute("request", requestDto);
            model.addAttribute("idRequest", idRequest);
            return "edit_request_by_id";
        }
        catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/error";
        }
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAnyAuthority('User')")
    @Operation(summary = "Обновить заявку", description = "Обновляет заявку на основе предоставленных данных")
    public String updateRequest(@Parameter(description = "ID заявки для обновления") @RequestParam Long idRequest,
                                @Parameter(description = "Данные для обновления заявки") RequestDto requestDto,
                                RedirectAttributes redirectAttributes) {
        try {
            Request requestToUpdate = updateRequestService.getRequestById(idRequest);
            if (!UpdateRequestService.isUpdated(requestToUpdate, requestDto)) {
                updateRequestService.updateRequestFromDto(requestToUpdate, requestDto);
                log.info("Request with ID: {} has been updated", idRequest);
            } else {
                log.info("No changes detected for request with ID: {}. Skipping database update.", idRequest);
            }
            return "redirect:/api/createdRequest/" + requestToUpdate.getId();
        }
        catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/error";
        }
    }
}
