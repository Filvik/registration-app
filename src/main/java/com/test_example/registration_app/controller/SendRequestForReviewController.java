package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.service.RequestDtoConverterService;
import com.test_example.registration_app.service.SendRequestForReviewService;
import com.test_example.registration_app.service.UpdateRequestService;
import lombok.RequiredArgsConstructor;
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
    public String getRequestById(@RequestParam Long idRequest, Model model) {
        Request requestFromDb = updateRequestService.getRequestById(idRequest);
        RequestDto request = requestDtoConverterService.fromRequestToRequestDto(requestFromDb);
        model.addAttribute("request", request);
        model.addAttribute("idRequest", idRequest);
        return "send_for_review";
    }

    @PostMapping("/review")
    public String sendRequestForReview(@RequestParam Long idRequest, RedirectAttributes redirectAttributes) {
        try {
            Request request = sendRequestForReviewService.send(idRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Request with ID: " + idRequest + " has been sent for review.");
            return "redirect:/api/created-request/" + request.getId();
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/error";
        }
    }
}
