package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RequestPageDTO;
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
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
@Tag(name = "ReceiveAllRequestsFromSomeoneController", description = "Контроллер для получения всех заявок от определенного пользователя")
public class ReceiveAllRequestsFromSomeoneController {

    private final RequestManipulationService requestManipulationService;
    private final RequestPagesConverterService requestPagesConverterService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('User')")
    @Operation(summary = "Получение всех заявок пользователя",
            description = "Позволяет пользователю получить список всех своих заявок с пагинацией и сортировкой. " +
                    "Доступен только юзерам.")
    public String getRequests(Model model,
                              Authentication authentication,
                              @Parameter(description = "Номер страницы для пагинации, по умолчанию 0")
                              @RequestParam(value = "page", defaultValue = "0") int defaultPage,
                              @Parameter(description = "Критерий сортировки, по умолчанию по дате создания, возрастание")
                              @RequestParam(value = "sort", defaultValue = "createdAt,asc") String sort) {
        try {
            Pageable pageable = requestManipulationService.getPageable(5, Sort.Direction.ASC, sort, defaultPage);
            Page<Request> requestPage = requestManipulationService.findRequestsByUserName(authentication.getName(), pageable);
            RequestPageDTO requestPageDTO = requestPagesConverterService.toRequestPageDTO(requestPage);

            model.addAttribute("requests", requestPageDTO.getContent());
            model.addAttribute("currentPage", requestPageDTO.getCurrentPage());
            model.addAttribute("totalPages", requestPageDTO.getTotalPages());
            model.addAttribute("sort", sort);
            return "requests_for_user";
        } catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "error";
        }
    }
}
