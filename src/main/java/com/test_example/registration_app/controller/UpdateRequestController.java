package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.enums.EnumStatus;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.service.CheckFromAuthService;
import com.test_example.registration_app.service.RequestConverterService;
import com.test_example.registration_app.service.RequestManipulationService;
import com.test_example.registration_app.service.UpdateRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private final CheckFromAuthService checkFromAuthService;
    private final RequestManipulationService requestManipulationService;

    @GetMapping("/edit")
    @PreAuthorize("hasAnyAuthority('User')")
    @Operation(summary = "Показать форму редактирования заявки",
            description = "Показывает форму для редактирования заявки пользователем. Доступен только юзерам.")
    public String showEditForm(@Parameter(description = "ID заявки для редактирования") @RequestParam Long idRequest,
                               Model model,
                               Authentication authentication) {
        try {
            Request request = updateRequestService.getRequestById(idRequest);
            if (checkFromAuthService.checkRequest(idRequest, authentication.getName())) {
                if (!request.getStatus().equals(EnumStatus.DRAFT)) {
                    return "redirect:/api/createdRequest/" + request.getId();
                }
                RequestDto requestDto = requestConverterService.fromRequestToRequestDto(request);
                model.addAttribute("request", requestDto);
                model.addAttribute("idRequest", idRequest);
                return "edit_request_by_id";
            } else {
                model.addAttribute("errorMessage", "The user is not the owner of this application.");
                return "error";
            }
        } catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAnyAuthority('User')")
    @Operation(summary = "Обновить заявку",
            description = "Обновляет заявку на основе предоставленных данных. Доступен только юзерам.")
    public String updateRequest(@Parameter(description = "ID заявки для обновления") @RequestParam Long idRequest,
                                @Parameter(description = "Данные для обновления заявки") @RequestBody RequestDto requestDto,    // для использование в swagger
//                                @Parameter(description = "Данные для обновления заявки") @ModelAttribute RequestDto requestDto,    // для использование в браузуре
                                Model model,
                                Authentication authentication) {
        try {
            Request requestToUpdate = updateRequestService.getRequestById(idRequest);
            if (checkFromAuthService.checkRequest(idRequest, authentication.getName()) &&
                    requestManipulationService.checkNameOwner(requestDto.getUserName(), requestToUpdate.getUser().getFullName())) {
                if (!UpdateRequestService.isUpdated(requestToUpdate, requestDto)) {
                    updateRequestService.updateRequestFromDto(requestToUpdate, requestDto);
                    log.info("Request ID: {} has been updated.", idRequest);
                } else {
                    log.info("No changes were detected for request with ID: {}. Skipping database update.", idRequest);
                }
                return "redirect:/api/createdRequest/" + requestToUpdate.getId();
            }
            else {
                model.addAttribute("errorMessage", "The user is not the owner of this application.");
                return "error";
            }
        } catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "error";
        }
    }
}
