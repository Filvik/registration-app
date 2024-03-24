package com.test_example.registration_app.service;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.repository.RequestRepository;
import com.test_example.registration_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RequestManipulationService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<Request> findRequestsByUserName(String name, Pageable pageable) {
        return userRepository.findByFullName(name)
                .map(user -> requestRepository.findAllByUserId(user.getId(), pageable))
                .orElse(Page.empty());
    }



//    public Request updateDraftRequest(Long id, Request requestUpdate, String name) {
//
//    }
//
//    public Request sendRequest(Long id, String name) {
//
//    }
}
