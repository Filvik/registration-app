package com.test_example.registration_app.service;

import com.test_example.registration_app.enums.EnumStatus;
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

import java.util.ArrayList;
import java.util.List;

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

    public Pageable getPageable(int defaultSize, Sort.Direction sortDirection, String sortTime, int defaultPage) {
        String[] sortParams = sortTime.split(",");
        if (sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1])) {
            sortDirection = Sort.Direction.DESC;
        }
        return PageRequest.of(defaultPage, defaultSize, Sort.by(sortDirection, sortParams[0]));
    }

    public Pageable getPageable(int defaultSize, String sortTime, String sortName, int defaultPage) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sortTime != null && !sortTime.isEmpty()) {
            String[] sortTimeParams = sortTime.split(",");
            Sort.Direction sortTimeDirection = Sort.Direction.fromString(sortTimeParams[1]);
            orders.add(new Sort.Order(sortTimeDirection, sortTimeParams[0]));
        }
        if (sortName != null && !sortName.isEmpty()) {
            String[] sortNameParams = sortName.split(",");
            Sort.Direction sortNameDirection = Sort.Direction.fromString(sortNameParams[1]);
            orders.add(new Sort.Order(sortNameDirection, sortNameParams[0]));
        }
        Pageable pageable;
        if (!orders.isEmpty()) {
            Sort sort = Sort.by(orders);
            pageable = PageRequest.of(defaultPage, defaultSize, sort);
        } else {
            pageable = PageRequest.of(defaultPage, defaultSize);
        }
        return pageable;
    }

    @Transactional(readOnly = true)
    public Page<Request> findRequestsByFilter(String filterName, Pageable pageable, boolean isAdmin) {
        if (isAdmin) {
            if (filterName != null && !filterName.isBlank()) {
                return requestRepository.findByUserFullNameContainingIgnoreCaseAndStatusNot(filterName, EnumStatus.DRAFT, pageable);
            } else {
                return requestRepository.findByStatusNot(EnumStatus.DRAFT, pageable);
            }
        } else {
            if (filterName != null && !filterName.isBlank()) {
                return requestRepository.findByUserFullNameContainingIgnoreCaseAndStatus(filterName, EnumStatus.SENT, pageable);
            } else {
                return requestRepository.findByStatus(EnumStatus.SENT, pageable);
            }
        }
    }

    public boolean checkNameOwner(String nameInBD, String nameOwnerRequest) {
        return nameOwnerRequest.equals(nameInBD);
    }
}
