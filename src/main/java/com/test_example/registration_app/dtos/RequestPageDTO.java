package com.test_example.registration_app.dtos;

import lombok.Data;

import java.util.List;

@Data
public class RequestPageDTO {
    private List<RequestDto> content;
    private int currentPage;
    private int totalPages;
}

