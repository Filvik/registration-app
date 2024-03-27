package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.CustomPageDTO;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.service.RequestManipulationService;
import com.test_example.registration_app.service.RequestPagesConverterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class OperatorReviewAllController {

    private final RequestManipulationService requestManipulationService;
    private final RequestPagesConverterService requestPagesConverterService;

    @GetMapping("/fromUser")
    @PreAuthorize("hasAnyAuthority('Operator', 'Administrator')")
    public String getRequests(Model model, Authentication authentication, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "page", defaultValue = "0") int defaultPage,
                              @RequestParam(value = "sort", defaultValue = "createdAt,asc") String sort) {
        try {
            Pageable pageable = requestManipulationService.getPageable(5, Sort.Direction.ASC, sort, defaultPage);
            Page<Request> requestPage = requestManipulationService.findAllRequests(pageable);
            CustomPageDTO customPageDTO = requestPagesConverterService.toCustomPageDTO(requestPage);

            model.addAttribute("requests", customPageDTO.getContent());
            model.addAttribute("currentPage", customPageDTO.getCurrentPage());
            model.addAttribute("totalPages", customPageDTO.getTotalPages());
            model.addAttribute("sort", sort);
            return "requests_for_operator";
        } catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/error";
        }
    }
}
