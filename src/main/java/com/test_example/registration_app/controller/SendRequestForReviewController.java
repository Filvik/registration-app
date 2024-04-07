package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.service.RequestConverterService;
import com.test_example.registration_app.service.SendRequestForReviewService;
import com.test_example.registration_app.service.UpdateRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@Controller
@RequestMapping("/send")
@RequiredArgsConstructor
@Tag(name = "SendRequestForReviewController", description = "Контроллер для отправки запросов на рассмотрение")
public class SendRequestForReviewController {

    private final SendRequestForReviewService sendRequestForReviewService;
    private final UpdateRequestService updateRequestService;
    private final RequestConverterService requestConverterService;

    @GetMapping("/review")
    @PreAuthorize("hasAnyAuthority('User')")
    @Operation(summary = "Получение заявки по ID", description = "Получение заявки по ID для отправки на рассмотрение")
    public String getRequestById(@Parameter(description = "ID заявки для получения") @RequestParam Long idRequest,
                                 Model model,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            Request requestFromDb = updateRequestService.getRequestById(idRequest);
            if (updateRequestService.checkName(requestFromDb.getUser().getFullName(), authentication.getName())) {
                RequestDto request = requestConverterService.fromRequestToRequestDto(requestFromDb);
                model.addAttribute("request", request);
                model.addAttribute("idRequest", idRequest);
                return "send_for_review";
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

    @PostMapping("/review")
    @PreAuthorize("hasAnyAuthority('User')")
    @Operation(summary = "Отправка заявки на рассмотрение", description = "Отправка заявки на рассмотрение по ее ID")
    public String sendRequestForReview(@Parameter(description = "ID заявки для отправки на рассмотрение")
                                       @RequestParam Long idRequest,
                                       RedirectAttributes redirectAttributes,
                                       Authentication authentication) {
        try {
            Request request = sendRequestForReviewService.sendForReview(idRequest, authentication.getName());
            return "redirect:/api/created-request/" + request.getId();
        } catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/error";
        }
    }
}
