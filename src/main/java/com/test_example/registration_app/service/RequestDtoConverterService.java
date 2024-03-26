package com.test_example.registration_app.service;

import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.model.User;
import com.test_example.registration_app.repository.UserRepository;
import com.test_example.registration_app.enums.EnumStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestDtoConverterService {

    private final UserRepository userRepository;

    public Request fromRequestDtoToRequest(RequestDto dto) {
        validateRequestDto(dto);

        User user = userRepository.findByFullName(dto.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + dto.getUserName() + " not found"));

        Request request = new Request();
        request.setUser(user);
        request.setUuid(dto.getUuid());
        request.setText(dto.getText());
        request.setCreatedAt(dto.getCreatedAt());
            try {
                request.setStatus(EnumStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.error("Invalid status value: " + dto.getStatus());
                throw new IllegalArgumentException("Invalid status value: " + dto.getStatus());
            }
        return request;
    }

    public RequestDto fromRequestToRequestDto(Request request) {
        validateRequest(request);

        RequestDto dto = new RequestDto();
        dto.setUuid(request.getUuid());
        dto.setUserName(request.getUser().getFullName());
        dto.setPhoneNumber(request.getUser().getPhoneNumber());
        dto.setEmail(request.getUser().getEmail());
        dto.setStatus(String.valueOf(request.getStatus()));
        dto.setText(request.getText());
        dto.setCreatedAt(request.getCreatedAt());

        return dto;
    }

    protected static void validateRequestDto(RequestDto dto) {
        if (dto == null || !StringUtils.hasText(dto.getUserName()) || !StringUtils.hasText(dto.getStatus()) ||
                !StringUtils.hasText(dto.getText())) {
            log.error("RequestDto contains invalid or null fields");
            throw new IllegalArgumentException("RequestDto contains invalid or null fields");
        }
    }

    protected static void validateRequest(Request request) {
        if (request == null || request.getUser() == null || request.getStatus() == null ||
                !StringUtils.hasText(request.getText())) {
            log.error("Request contains invalid or null fields");
            throw new IllegalArgumentException("Request contains invalid or null fields");
        }
    }
}
