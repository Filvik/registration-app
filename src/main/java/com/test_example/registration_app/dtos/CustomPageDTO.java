package com.test_example.registration_app.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CustomPageDTO {
    private List<RequestDto> content;
    private int currentPage;
    private int totalPages;
}

