package com.test_example.registration_app.service;

import com.test_example.registration_app.model.Role;
import com.test_example.registration_app.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Set<Role> getUserRoles(List<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            roleRepository.findByRoleName(roleName).ifPresent(roles::add);
        }
        return roles;
    }
}
