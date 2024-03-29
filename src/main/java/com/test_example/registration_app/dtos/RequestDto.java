package com.test_example.registration_app.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
public class RequestDto {
    private UUID uuid;
    private String userName;
    private String phoneNumber;
    private String email;
    private String status;
    private String text;
    private Timestamp createdAt;
}
