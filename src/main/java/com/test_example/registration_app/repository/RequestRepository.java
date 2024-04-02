package com.test_example.registration_app.repository;

import com.test_example.registration_app.enums.EnumStatus;
import com.test_example.registration_app.model.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Page<Request> findAllByUserId(Long id, Pageable pageable);
    Page<Request> findByUserFullNameContainingIgnoreCase(String filterName, Pageable pageable);
    Page<Request> findByStatus(EnumStatus enumStatus, Pageable pageable);
    Page<Request> findByUserFullNameContainingIgnoreCaseAndStatus(String filterName, EnumStatus enumStatus, Pageable pageable);
    Page<Request> findByUserFullNameContainingIgnoreCaseAndStatusNot(String filterName, EnumStatus enumStatus, Pageable pageable);
    Page<Request> findByStatusNot(EnumStatus enumStatus, Pageable pageable);

}

