package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.service.RequestDtoConverterService;
import com.test_example.registration_app.service.SendRequestForReviewService;
import com.test_example.registration_app.service.UpdateRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/send")
@RequiredArgsConstructor
public class SendRequestForReviewController {

    private final SendRequestForReviewService sendRequestForReviewService;
    private final UpdateRequestService updateRequestService;
    private final RequestDtoConverterService requestDtoConverterService;

    @GetMapping("/review")
    public String getRequestById(@RequestParam Long idRequest, Model model, Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            Request requestFromDb = updateRequestService.getRequestById(idRequest);
            if (updateRequestService.checkName(requestFromDb.getUser().getFullName(), authentication.getName())) {
                RequestDto request = requestDtoConverterService.fromRequestToRequestDto(requestFromDb);
                model.addAttribute("request", request);
                model.addAttribute("idRequest", idRequest);
                return "send_for_review";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Not available for viewing");
                return "redirect:/error";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/error";
        }
    }

    @PostMapping("/review")
    public String sendRequestForReview(@RequestParam Long idRequest, RedirectAttributes redirectAttributes,
                                       Authentication authentication) {
        try {
            Request request = sendRequestForReviewService.sendForReview(idRequest, authentication.getName());
            return "redirect:/api/created-request/" + request.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/error";
        }
    }
}
