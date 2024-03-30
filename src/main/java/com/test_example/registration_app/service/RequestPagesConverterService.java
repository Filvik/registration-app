package com.test_example.registration_app.service;

import com.test_example.registration_app.dtos.RequestPageDTO;
import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.model.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestPagesConverterService {

    private final RequestDtoConverterService requestDtoConverterService;

    public RequestPageDTO toRequestPageDTO(Page<Request> requestPage) {
        RequestPageDTO requestPageDTO = new RequestPageDTO();
        requestPageDTO.setContent(requestPage.getContent().stream().map(this::toDto).collect(Collectors.toList()));
        requestPageDTO.setCurrentPage(requestPage.getNumber());
        requestPageDTO.setTotalPages(requestPage.getTotalPages());
        return requestPageDTO;
    }

    private RequestDto toDto(Request request) {
        return requestDtoConverterService.fromRequestToRequestDto(request);
    }
}
