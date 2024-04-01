package com.test_example.registration_app.service;

import com.test_example.registration_app.dtos.UserDto;
import com.test_example.registration_app.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersConverterService {

    private final RoleConverterService roleConverterService;

    @Transactional(readOnly = true)
    public List<UserDto> convertFromUserToUserDto(List<User> users) {
        return users.stream()
                .map(this::convertFromUserToUserDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
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
