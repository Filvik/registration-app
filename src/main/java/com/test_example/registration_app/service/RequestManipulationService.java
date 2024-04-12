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

    /**
     * Находит страницу с запросами по имени пользователя.
     *
     * @param name Имя пользователя для поиска.
     * @param pageable Объект Pageable, определяющий параметры пагинации и сортировки.
     * @return Страница с запросами.
     */
    @Transactional(readOnly = true)
    public Page<Request> findRequestsByUserName(String name, Pageable pageable) {
        return userRepository.findByFullName(name)
                .map(user -> requestRepository.findAllByUserId(user.getId(), pageable))
                .orElse(Page.empty());
    }

    /**
     * Создает объект Pageable с параметрами сортировки.
     *
     * @param defaultSize Размер страницы.
     * @param sortDirection Направление сортировки.
     * @param sortTime Параметр сортировки по времени.
     * @param defaultPage Номер страницы.
     * @return Объект Pageable.
     */
    public Pageable getPageable(int defaultSize, Sort.Direction sortDirection, String sortTime, int defaultPage) {
        String[] sortParams = sortTime.split(",");
        if (sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1])) {
            sortDirection = Sort.Direction.DESC;
        }
        return PageRequest.of(defaultPage, defaultSize, Sort.by(sortDirection, sortParams[0]));
    }

    /**
     * Создает объект Pageable с множественными параметрами сортировки.
     *
     * @param defaultSize Размер страницы.
     * @param sortTime Параметр сортировки по времени.
     * @param sortName Параметр сортировки по имени.
     * @param defaultPage Номер страницы.
     * @return Объект Pageable.
     */
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

    /**
     * Находит страницу с запросами по фильтру с учетом статуса администратора.
     *
     * @param filterName Фильтр по имени пользователя.
     * @param pageable Объект Pageable.
     * @param isAdmin Является ли пользователь администратором.
     * @return Страница с запросами.
     */
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

    /**
     * Проверяет соответствие имени в базе данных с именем в запросе.
     *
     * @param nameInBD Имя пользователя в базе данных.
     * @param nameOwnerRequest Имя пользователя в запросе.
     * @return true, если имена совпадают, иначе false.
     */
    public boolean checkNameOwner(String nameInBD, String nameOwnerRequest) {
        return nameOwnerRequest.equals(nameInBD);
    }
}
