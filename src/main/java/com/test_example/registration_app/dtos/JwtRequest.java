package com.test_example.registration_app.dtos;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
