package com.test_example.registration_app.service;

import com.test_example.registration_app.enums.EnumStatus;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendRequestForReviewService {

    private final UpdateRequestService updateRequestService;
    private final RequestRepository requestRepository;

    public Request send(Long idRequest) {
        Request request = updateRequestService.getRequestById(idRequest);
        if (request.getStatus().equals(EnumStatus.DRAFT)) {
            request.setStatus(EnumStatus.SENT);
            requestRepository.saveAndFlush(request);
        }
        else {
            log.info("Status has not changed for request ID: {}", idRequest);
            throw new IllegalStateException("Request status is not in DRAFT");
        }
        return request;
    }
}
