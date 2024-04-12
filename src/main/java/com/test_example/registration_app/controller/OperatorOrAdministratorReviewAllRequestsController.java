package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RequestPageDTO;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.service.CheckFromAuthService;
import com.test_example.registration_app.service.RequestManipulationService;
import com.test_example.registration_app.service.RequestPagesConverterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
@Tag(name = "OperatorOrAdministratorReviewAllRequestsController",
        description = "Контроллер для просмотра всех заявок оператором или администратором")
public class OperatorOrAdministratorReviewAllRequestsController {

    private final RequestManipulationService requestManipulationService;
    private final RequestPagesConverterService requestPagesConverterService;
    private final CheckFromAuthService checkFromAuthService;

    @GetMapping("/requests")
    @PreAuthorize("hasAnyAuthority('Operator','Administrator')")
    @Operation(summary = "Просмотр всех заявок",
            description = "Позволяет операторам и администраторам просматривать все заявки с возможностью сортировки и фильтрации." +
                    "Доступен только операторам и администраторам.")
    public String getRequests(Model model, Authentication authentication,
                              @Parameter(description = "Пагинация, {5,10,20}") @RequestParam(value = "paginationSize", defaultValue = "5") int paginationSize,
                              @Parameter(description = "Номер страницы для пагинации, {0...n}") @RequestParam(value = "page", defaultValue = "0") int defaultPage,
                              @Parameter(description = "Критерий сортировки по времени. {createdAt,asc или ,desc}") @RequestParam(value = "sortTime", required = false) String sortTime,
                              @Parameter(description = "Критерий сортировки по имени. {user.fullName,asc или ,desc}") @RequestParam(value = "sortName", required = false) String sortName,
                              @Parameter(description = "Фильтрация по имени. {Имя искомого пользователя}") @RequestParam(value = "filterName", required = false) String filterName) {
        try {
            Pageable pageable = requestManipulationService.getPageable(paginationSize, sortTime, sortName, defaultPage);
            boolean isAdmin = checkFromAuthService.checkRole(authentication, "Administrator");
            Page<Request> requestPage = requestManipulationService.findRequestsByFilter(filterName, pageable, isAdmin);
            if (defaultPage >= requestPage.getTotalPages()) {
                model.addAttribute("errorMessage", "No applications found on the specified page.");
                return "error";
            }
            RequestPageDTO requestPageDTO = requestPagesConverterService.toRequestPageDTO(requestPage);
            model.addAttribute("requests", requestPageDTO.getContent());
            model.addAttribute("currentPage", requestPageDTO.getCurrentPage());
            model.addAttribute("totalPages", requestPageDTO.getTotalPages());
            model.addAttribute("sortTime", sortTime);
            model.addAttribute("sortName", sortName);
            model.addAttribute("filterName", filterName);
            return "requests_for_operator_or_administrator";
        } catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "error";
        }
    }
}
