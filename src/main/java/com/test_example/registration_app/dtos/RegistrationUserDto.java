package com.test_example.registration_app.dtos;

import com.test_example.registration_app.model.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RegistrationUserDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private Set<Role> roles = new HashSet<>();
}
