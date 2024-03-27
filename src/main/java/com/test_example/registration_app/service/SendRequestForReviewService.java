package com.test_example.registration_app.service;

import com.test_example.registration_app.enums.EnumStatus;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.repository.RequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendRequestForReviewService {

    private final UpdateRequestService updateRequestService;
    private final RequestRepository requestRepository;

    @Transactional
    public Request sendForReview(Long idRequest, String name) {
        Request request = updateRequestService.getRequestById(idRequest);
        if (updateRequestService.checkName(request.getUser().getFullName(), name)) {
            if (request.getStatus().equals(EnumStatus.DRAFT)) {
                request.setStatus(EnumStatus.SENT);
                requestRepository.saveAndFlush(request);
            } else {
                log.info("Status has not changed for request ID: {}", idRequest);
                throw new IllegalStateException("Request status is not in DRAFT");
            }
        } else {
            log.info("Status has not changed for request ID: {}", idRequest);
            throw new IllegalStateException("Not available for update. You are not owner of request");
        }
        return request;
    }

    @Transactional
    public Optional<Request> sendTest(Long idRequest, String username) {
        return requestRepository.findById(idRequest).filter(request -> request.getUser().getFullName().equals(username))
                .filter(request -> request.getStatus().equals(EnumStatus.DRAFT))
                .map(request -> {
                    request.setStatus(EnumStatus.SENT);
                    return requestRepository.save(request);
                });
    }
}
