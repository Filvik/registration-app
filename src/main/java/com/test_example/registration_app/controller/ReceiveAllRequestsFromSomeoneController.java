package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.CustomPageDTO;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.service.RequestManipulationService;
import com.test_example.registration_app.service.RequestPagesConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ReceiveAllRequestsFromSomeoneController {

    private final RequestManipulationService requestManipulationService;
    private final RequestPagesConverterService requestPagesConverterService;

    @GetMapping
    public String getRequests(Model model, Authentication authentication,
                              @RequestParam(value = "page", defaultValue = "0") int defaultPage,
                              @RequestParam(value = "sort", defaultValue = "createdAt,asc") String sort) {

        Pageable pageable = requestManipulationService.getPageable(5,Sort.Direction.ASC, sort, defaultPage);
        Page<Request> requestPage = requestManipulationService.findRequestsByUserName(authentication.getName(), pageable);
        CustomPageDTO customPageDTO = requestPagesConverterService.toCustomPageDTO(requestPage);

        model.addAttribute("requests", customPageDTO.getContent());
        model.addAttribute("currentPage", customPageDTO.getCurrentPage());
        model.addAttribute("totalPages", customPageDTO.getTotalPages());
        model.addAttribute("sort", sort);
        return "requests";
    }
}
