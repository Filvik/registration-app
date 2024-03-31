package com.test_example.registration_app.service;

import com.test_example.registration_app.dtos.UserDto;
import com.test_example.registration_app.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UsersConverterService {

    public List<UserDto> convertFromUserToUserDto(List<User> users) {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto dto = new UserDto(
                    user.getFullName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getCreatedAt(),
                    user.getUpdatedAt()
            );
            userDtos.add(dto);
        }
        return userDtos;
    }

    public UserDto convertFromUserToUserDto(User user) {
        return new UserDto(
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
