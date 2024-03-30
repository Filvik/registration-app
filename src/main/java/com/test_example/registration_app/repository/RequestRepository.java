package com.test_example.registration_app.repository;

import com.test_example.registration_app.model.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Page<Request> findAllByUserId(Long id, Pageable pageable);
    Page<Request> findByUserFullNameContainingIgnoreCase(String filterName, Pageable pageable);
}
