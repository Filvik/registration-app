package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.service.RequestDtoConverterService;
import com.test_example.registration_app.service.UpdateRequestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.test_example.registration_app.model.Request;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/api")
public class CreatedRequestViewController {

    private final RequestDtoConverterService requestDtoConverterService;
    private final UpdateRequestService updateRequestService;

    @GetMapping("/created-request/{id}")
    public String getRequestById(@PathVariable Long id, Model model, Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        Request requestFromDb = updateRequestService.getRequestById(id);

        if (requestFromDb.getUser().getFullName().equals(authentication.getName())) {
            RequestDto request = requestDtoConverterService.fromRequestToRequestDto(requestFromDb);
            model.addAttribute("request", request);
            return "request_details";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Not available for viewing");
            return "redirect:/error";
        }
    }
}
