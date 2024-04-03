package com.test_example.registration_app.controller;

import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.model.RequestCreationResponse;
import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.service.CreateRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Controller
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
@Tag(name = "CreateRequestController", description = "Контроллер для создания заявок")
public class CreateRequestController {

    private final CreateRequestService createRequestService;

    @GetMapping("/create-request-form")
    @PreAuthorize("hasAnyAuthority('User')")
    @Operation(summary = "Форма создания заявки", description = "Отображает форму для создания новой заявки пользователем")
    public String showCreateRequestForm() {
        return "create_request_form";
    }

    @PostMapping("/create-request")
    @PreAuthorize("hasAnyAuthority('User')")
    @Operation(summary = "Создание заявки",
            description = "Создает новую заявку на основе предоставленных данных",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO заявки для создания", required = true, content = @Content(schema = @Schema(implementation = RequestDto.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Заявка успешно создана", content = @Content(schema = @Schema(implementation = RequestCreationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Неверный запрос")
            })
    public ResponseEntity<?> createRequest(@Parameter(description = "DTO новой заявки") @RequestBody RequestDto requestDto) {
        try {
            Request request = createRequestService.createRequest(requestDto);
            String requestUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/created-request/{id}")
                    .buildAndExpand(request.getId())
                    .toUriString();
            return ResponseEntity.created(URI.create(requestUrl))
                    .body(new RequestCreationResponse("Successful", requestUrl));
        } catch (Exception e) {
            log.error("Failed to create request: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new RequestCreationResponse("Failed"));
        }
    }
}
