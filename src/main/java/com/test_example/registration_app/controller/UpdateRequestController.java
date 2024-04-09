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
                               Model model) {
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
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAnyAuthority('User')")
    @Operation(summary = "Обновить заявку", description = "Обновляет заявку на основе предоставленных данных")
    public String updateRequest(@Parameter(description = "ID заявки для обновления") @RequestParam Long idRequest,
                                @Parameter(description = "Данные для обновления заявки") @RequestBody RequestDto requestDto,    // для использование в swagger
//                                @Parameter(description = "Данные для обновления заявки") @ModelAttribute RequestDto requestDto,    // для использование в браузуру
                                Model model) {
        try {
            Request requestToUpdate = updateRequestService.getRequestById(idRequest);
            if (!UpdateRequestService.isUpdated(requestToUpdate, requestDto)) {
                updateRequestService.updateRequestFromDto(requestToUpdate, requestDto);
                log.info("Запрос с идентификатором: {} обновлен.", idRequest);
            } else {
                log.info("Для запроса с идентификатором: {} изменений не обнаружено. Пропуск обновления базы данных.", idRequest);
            }
            return "redirect:/api/createdRequest/" + requestToUpdate.getId();
        }
        catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "error";
        }
    }
}
