package com.test_example.registration_app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestPageDTO {
    private List<RequestDto> content;
    private int currentPage;
    private int totalPages;
}

