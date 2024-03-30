package com.test_example.registration_app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddNewUserDto {
    private String description;
    private String username;
}
