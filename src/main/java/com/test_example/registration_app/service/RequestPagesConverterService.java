package com.test_example.registration_app.service;

import com.test_example.registration_app.dtos.CustomPageDTO;
import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.model.Request;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class RequestPagesConverterService {

    public CustomPageDTO toCustomPageDTO(Page<Request> requestPage) {
        CustomPageDTO customPageDTO = new CustomPageDTO();
        customPageDTO.setContent(requestPage.getContent().stream().map(this::toDto).collect(Collectors.toList()));
        customPageDTO.setCurrentPage(requestPage.getNumber());
        customPageDTO.setTotalPages(requestPage.getTotalPages());
        return customPageDTO;
    }

    private RequestDto toDto(Request request) {
        return RequestDtoConverterService.fromRequestToRequestDto(request);
    }
}
