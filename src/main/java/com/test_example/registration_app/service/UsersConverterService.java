package com.test_example.registration_app.service;

import com.test_example.registration_app.dtos.UserDto;
import com.test_example.registration_app.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersConverterService {

    private final RoleConverterService roleConverterService;

    /**
     * Конвертирует список пользователей в список DTO.
     * Использует метод convertFromUserToUserDto для конвертации каждого пользователя.
     *
     * @param users Список пользователей для конвертации.
     * @return Список DTO пользователей.
     */
    public List<UserDto> convertFromUserToUserDto(List<User> users) {
        return users.stream()
                .map(this::convertFromUserToUserDto)
                .collect(Collectors.toList());
    }

    /**
     * Конвертирует одного пользователя в DTO.
     * Данные о ролях пользователя также конвертируются с помощью RoleConverterService.
     *
     * @param user Пользователь, который должен быть конвертирован.
     * @return DTO пользователя.
     */
    public UserDto convertFromUserToUserDto(User user) {
        return new UserDto(
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                roleConverterService.convertFromRoleToRoleDto(user.getRoles()),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
