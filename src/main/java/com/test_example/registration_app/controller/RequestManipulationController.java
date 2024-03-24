package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.CustomPageDTO;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.service.RequestManipulationService;
import com.test_example.registration_app.service.RequestPagesConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestManipulationController {

    private final RequestManipulationService requestManipulationService;
    private final RequestPagesConverterService requestPagesConverterService;

    @GetMapping
    public String getRequests(Model model, Authentication authentication,
                              @RequestParam(value = "page", defaultValue = "0") int defaultpage,
                              @RequestParam(value = "sort", defaultValue = "createdAt,asc") String sort) {
        int defaultSize = 5;
        Sort.Direction sortDirection = Sort.Direction.ASC;
        String[] sortParams = sort.split(",");
        if (sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1])) {
            sortDirection = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(defaultpage, defaultSize, Sort.by(sortDirection, sortParams[0]));
        Page<Request> requestPage = requestManipulationService.findRequestsByUserName(authentication.getName(), pageable);
        CustomPageDTO customPageDTO = requestPagesConverterService.toCustomPageDTO(requestPage);

        model.addAttribute("requests", customPageDTO.getContent());
        model.addAttribute("currentPage", customPageDTO.getCurrentPage());
        model.addAttribute("totalPages", customPageDTO.getTotalPages());
        model.addAttribute("sort", sort);
        return "requests";
    }



//    // Редактирование заявки в статусе "черновик" пользователем
//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<Request> editDraftRequest(@PathVariable Long id,
//                                                    @RequestBody Request requestUpdate,
//                                                    Authentication authentication) {
//        return ResponseEntity.ok(requestManipulationService.updateDraftRequest(id, requestUpdate, authentication.getName()));
//    }
//
//    // Отправка заявки на рассмотрение
//    @PostMapping("/{id}/send")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<Request> sendRequest(@PathVariable Long id, Authentication authentication) {
//        return ResponseEntity.ok(requestManipulationService.sendRequest(id, authentication.getName()));
//    }
}
