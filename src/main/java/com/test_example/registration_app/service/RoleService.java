package com.test_example.registration_app.service;

import com.test_example.registration_app.model.Role;
import com.test_example.registration_app.model.User;
import com.test_example.registration_app.repository.RoleRepository;
import com.test_example.registration_app.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Data
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public Set<Role> getUserRolesForAddInBD(List<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            roleRepository.findByRoleName(roleName).ifPresent(roles::add);
        }
        return roles;
    }

    public Set<Role> getUserRolesFromBD(Long id){
        Optional<User> user = userRepository.findById(id);
        return user.map(User::getRoles).orElse(Collections.emptySet());
    }
}
