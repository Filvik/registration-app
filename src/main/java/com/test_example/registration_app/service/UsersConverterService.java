package com.test_example.registration_app.service;

import com.test_example.registration_app.dtos.UsersDto;
import com.test_example.registration_app.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UsersConverterService {

    public List<UsersDto> convertFromUserToUserDto(List<User> users) {
        List<UsersDto> usersDtos = new ArrayList<>();
        for (User user : users) {
            UsersDto dto = new UsersDto(
                    user.getFullName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getCreatedAt(),
                    user.getUpdatedAt()
            );
            usersDtos.add(dto);
        }
        return usersDtos;
    }
}
