package com.test_example.registration_app.service;

import com.test_example.registration_app.model.Role;
import com.test_example.registration_app.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleManipulationService {

    private final RoleRepository roleRepository;

    public Set<Role> getRoleFromDB(Long idUser) {
        return roleRepository.findByUserId(idUser);
    }
}
