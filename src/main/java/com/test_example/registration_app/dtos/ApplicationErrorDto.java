package com.test_example.registration_app.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ApplicationErrorDto {
    private int status;
    private String message;
    private Date timestamp;

    public ApplicationErrorDto(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
