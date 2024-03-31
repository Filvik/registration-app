package com.test_example.registration_app.service;

import com.test_example.registration_app.dtos.RoleDto;
import com.test_example.registration_app.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleConverterService {

    public Set<RoleDto> convertFromRoleToRoleDto(Set<Role> roles) {
        return roles.stream()
                .map(role -> new RoleDto(role.getRoleName()))
                .collect(Collectors.toSet());
    }
}
