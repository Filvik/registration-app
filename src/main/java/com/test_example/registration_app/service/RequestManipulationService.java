package com.test_example.registration_app.service;

import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.repository.RequestRepository;
import com.test_example.registration_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Transactional(readOnly = true)
    public Page<Request> findAllRequests(Pageable pageable) {
        return requestRepository.findAll(pageable);
    }

    public Pageable getPageable(int defaultSize, Sort.Direction sortDirection,  String sort, int defaultPage) {
        String[] sortParams = sort.split(",");
        if (sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1])) {
            sortDirection = Sort.Direction.DESC;
        }
        return PageRequest.of(defaultPage, defaultSize, Sort.by(sortDirection, sortParams[0]));
    }
}
