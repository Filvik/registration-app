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

import static com.test_example.registration_app.enums.EnumStatus.SENT;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateRequestService {

    private final RequestRepository requestRepository;

    @Transactional
    public void updateRequestFromDto(Request requestToUpdate, RequestDto requestDto) {

        if (requestToUpdate == null || requestDto == null) {
            log.error("Попытка обновить запрос с помощью нулевой сущности или DTO.");
            throw new IllegalArgumentException("Попытка обновить запрос с помощью нулевой сущности или DTO.");
        }

        if (requestDto.getStatus().equals("SENT")){
            EnumStatus status = EnumStatus.valueOf(requestDto.getStatus().toUpperCase());
            log.info("Обновление запроса с помощью ID: {}", requestToUpdate.getId());
            try {
                requestToUpdate.setText(requestDto.getText());
                requestToUpdate.setStatus(status);
            } catch (IllegalArgumentException e) {
                log.error("Недопустимое значение статуса '{}' предоставлено для идентификатора запроса: {}", requestDto.getStatus(), requestToUpdate.getId());
                throw new IllegalArgumentException("Недопустимое значение статуса: " + requestDto.getStatus());
            }
            requestToUpdate.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            requestRepository.saveAndFlush(requestToUpdate);
        }
        else {
            log.error("Неверное значение статуса '{}'", requestDto.getStatus());
            throw new IllegalArgumentException("Неверное значение статуса: " + requestDto.getStatus());
        }
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

    @Transactional
    public Request sendAccept(Request request, String action) {
        if (request.getStatus().equals(SENT)) {
            if (action.equals("accept")) {
                request.setStatus(EnumStatus.ACCEPTED);
            } else if (action.equals("reject")) {
                request.setStatus(EnumStatus.REJECTED);
            }
            requestRepository.saveAndFlush(request);
        } else {
            log.error("Неверное значение статуса '{}'", request.getStatus());
            throw new IllegalArgumentException("Неверное значение статуса: " + request.getStatus());
        }
        return request;
    }
}
