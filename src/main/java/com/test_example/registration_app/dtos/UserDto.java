package com.test_example.registration_app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserDto {
    private String fullName;
    private String email;
    private String phoneNumber;
    private Set<RoleDto> roles;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
