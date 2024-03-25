package com.test_example.registration_app.service;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.enums.EnumStatus;
import com.test_example.registration_app.model.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
public class UpdateRequestService {

    public static Request updateRequestFromDto(Request requestToUpdate, RequestDto requestDto) {

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

        return requestToUpdate;
    }

    public static boolean isUpdated(Request requestToUpdate, RequestDto requestDto) {
        EnumStatus status = EnumStatus.valueOf(requestDto.getStatus().toUpperCase());
        return requestDto.getText().equals(requestToUpdate.getText()) &&
                status == requestToUpdate.getStatus();
    }
}
