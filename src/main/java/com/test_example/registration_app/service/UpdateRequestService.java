package com.test_example.registration_app.service;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.enums.EnumStatus;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.repository.RequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateRequestService {

    private final RequestRepository requestRepository;

    @Transactional
    public void updateRequestFromDto(Request requestToUpdate, RequestDto requestDto) {

        if (requestToUpdate == null || requestDto == null) {
            log.error("Attempted to update a request with a null entity or DTO.");
            throw new IllegalArgumentException("Request to update or RequestDto is null");
        }

        EnumStatus status = EnumStatus.valueOf(requestDto.getStatus().toUpperCase());
        log.info("Updating request with ID: {}", requestToUpdate.getId());
        try {
            requestToUpdate.setText(requestDto.getText());
            requestToUpdate.setStatus(status);
        } catch (IllegalArgumentException e) {
            log.error("Invalid status value '{}' provided for request ID: {}", requestDto.getStatus(), requestToUpdate.getId());
            throw new IllegalArgumentException("Invalid status value: " + requestDto.getStatus());
        }
        requestToUpdate.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        requestRepository.saveAndFlush(requestToUpdate);
    }

    public static boolean isUpdated(Request requestToUpdate, RequestDto requestDto) {
        EnumStatus status = EnumStatus.valueOf(requestDto.getStatus().toUpperCase());
        return requestDto.getText().equals(requestToUpdate.getText()) &&
                status == requestToUpdate.getStatus();
    }

    public Request getRequestById(Long idRequest) {
        return requestRepository.findById(idRequest)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request Id: " + idRequest));
    }

    public boolean checkName(String nameFromRequest, String nameFromAuth) {
        return nameFromRequest.equals(nameFromAuth);
    }

    public Request sendAccept(Request request, String action) {
        if (request.getStatus().equals(EnumStatus.SENT)) {
            if (action.equals("accept")) {
                request.setStatus(EnumStatus.ACCEPTED);
            } else if (action.equals("reject")) {
                request.setStatus(EnumStatus.REJECTED);
            }
            requestRepository.saveAndFlush(request);
        } else {
            log.error("Invalid status value '{}'", request.getStatus());
            throw new IllegalArgumentException("Invalid status value: " + request.getStatus());
        }
        return request;
    }
}
