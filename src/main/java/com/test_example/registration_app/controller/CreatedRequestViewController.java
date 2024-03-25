package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.repository.RequestRepository;
import com.test_example.registration_app.service.CreateRequestService;
import com.test_example.registration_app.service.RequestDtoConverterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.test_example.registration_app.model.Request;


@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/api")
public class CreatedRequestViewController {

    private final CreateRequestService createRequestService;
    private final RequestRepository requestRepository;

    @GetMapping("/created-request/{id}")
    public String getRequestById(@PathVariable Long id, Model model) {
        Request requestFromDb = requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request Id:" + id));
        RequestDto request = RequestDtoConverterService.fromRequestToRequestDto(requestFromDb);
        model.addAttribute("request", request);
        return "request_details";
    }
}
