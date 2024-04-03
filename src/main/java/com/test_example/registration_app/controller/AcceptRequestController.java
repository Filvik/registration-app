package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.enums.EnumStatus;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.service.RequestDtoConverterService;
import com.test_example.registration_app.service.UpdateRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/sendAccept")
@RequiredArgsConstructor
@Tag(name = "AcceptRequestController", description = "Контроллер для принятия или отклонения заявок оператором")
public class AcceptRequestController {

    private final RequestDtoConverterService requestDtoConverterService;
    private final UpdateRequestService updateRequestService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('Operator')")
    @Operation(summary = "Показать форму редактирования", description = "Показывает форму для принятия или отклонения заявки оператором")
    public String showEditForm(@Parameter(description = "ID заявки для редактирования")@RequestParam Long idRequest,
                               Model model, RedirectAttributes redirectAttributes) {
        try {
            Request request = updateRequestService.getRequestById(idRequest);
            if (request.getStatus().equals(EnumStatus.SENT)) {
                RequestDto requestDto = requestDtoConverterService.fromRequestToRequestDto(request);
                model.addAttribute("request", requestDto);
                model.addAttribute("idRequest", idRequest);
                return "send_accept_from_operator";
            } else {
                log.warn("Error status. Status: " + request.getStatus());
                redirectAttributes.addFlashAttribute("errorMessage", "Error status. Status: " + request.getStatus());
                return "redirect:/error";
            }
        } catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/error";
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('Operator')")
    @Operation(summary = "Принять или отклонить заявку", description = "Позволяет оператору принять или отклонить заявку")
    public String sendAcceptRequest( @Parameter(description = "ID заявки для действия") @RequestParam Long idRequest,
                                     @Parameter(description = "Действие с заявкой ('accept' или 'reject')") @RequestParam String action,
                                     RedirectAttributes redirectAttributes, Model model) {
        try {
            Request requestFromDb = updateRequestService.getRequestById(idRequest);
            Request requestSendAction = updateRequestService.sendAccept(requestFromDb, action);
            RequestDto request = requestDtoConverterService.fromRequestToRequestDto(requestSendAction);
            model.addAttribute("request", request);
            return "request_details";
        } catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/error";
        }
    }
}
