package com.test_example.registration_app.service;

import com.test_example.registration_app.dtos.RoleDto;
import com.test_example.registration_app.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleConverterService {

    /**
     * Конвертирует набор ролей в набор DTO.
     * Преобразует каждую роль в объект DTO, содержащий только имя роли.
     *
     * @param roles набор ролей для конвертации.
     * @return набор DTO ролей.
     */
    public Set<RoleDto> convertFromRoleToRoleDto(Set<Role> roles) {
        return roles.stream()
                .map(role -> new RoleDto(role.getRoleName()))
                .collect(Collectors.toSet());
    }
}
