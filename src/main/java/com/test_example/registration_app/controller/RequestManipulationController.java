package com.test_example.registration_app.controller;

import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.service.RequestManipulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestManipulationController {

    private final RequestManipulationService requestManipulationService;

    // Просмотр созданных пользователем заявок с пагинацией и сортировкой
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<Request>> getUserRequests(Authentication authentication,
                                                         @PageableDefault(sort = "createdAt",
                                                                 direction = Sort.Direction.ASC, size = 5) Pageable pageable) {
        return ResponseEntity.ok(requestManipulationService.findRequestsByUserName(authentication.getName(), pageable));
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
