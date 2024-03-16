package com.test_example.registration_app.controller;

import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.model.RequestCreationResponse;
import com.test_example.registration_app.model.RequestDto;
import com.test_example.registration_app.service.CreateRequestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class CreateRequestController {

    private final CreateRequestService createRequestService;

    @PostMapping("/create-request")
    public ResponseEntity<?> createRequest(@RequestBody RequestDto requestDto) {
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
