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
public class RequestConverterService {

    private final UserRepository userRepository;

    /**
     * Конвертирует объект DTO в объект запроса (Request).
     * Проверяет валидность DTO, извлекает пользователя по имени и создаёт новый объект запроса.
     *
     * @param dto объект DTO, содержащий данные для создания запроса.
     * @return объект запроса (Request).
     * @throws IllegalArgumentException если пользователь не найден или данные DTO невалидны.
     */
    public Request fromRequestDtoToRequest(RequestDto dto) {
        validateRequestDto(dto);

        User user = userRepository.findByFullName(dto.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + dto.getUserName() + " not found"));

        Request request = new Request();
        request.setUser(user);
        request.setUuid(dto.getUuid());
        request.setText(dto.getText());
        request.setCreatedAt(dto.getCreatedAt());
        request.setUpdatedAt(dto.getUpdatedAt());
            try {
                request.setStatus(EnumStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.error("Invalid status value: " + dto.getStatus());
                throw new IllegalArgumentException("Invalid status value: " + dto.getStatus());
            }
        return request;
    }

    /**
     * Конвертирует объект запроса в объект DTO.
     * Проверяет валидность объекта запроса и создаёт DTO с соответствующими данными.
     *
     * @param request объект запроса для конвертации в DTO.
     * @return объект DTO (RequestDto).
     * @throws IllegalArgumentException если объект запроса невалиден.
     */
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
        dto.setUpdatedAt(request.getUpdatedAt());

        return dto;
    }

    /**
     * Валидирует DTO запроса.
     *
     * @param dto DTO запроса для валидации.
     * @throws IllegalArgumentException если DTO содержит невалидные или неполные данные.
     */
    protected static void validateRequestDto(RequestDto dto) {
        if (dto == null || !StringUtils.hasText(dto.getUserName()) || !StringUtils.hasText(dto.getStatus()) ||
                !StringUtils.hasText(dto.getText())) {
            log.error("RequestDto contains invalid or null fields");
            throw new IllegalArgumentException("RequestDto contains invalid or null fields");
        }
    }

    /**
     * Валидирует объект запроса.
     *
     * @param request объект запроса для валидации.
     * @throws IllegalArgumentException если объект запроса содержит невалидные или неполные данные.
     */
    protected static void validateRequest(Request request) {
        if (request == null || request.getUser() == null || request.getStatus() == null ||
                !StringUtils.hasText(request.getText())) {
            log.error("Request contains invalid or null fields");
            throw new IllegalArgumentException("Request contains invalid or null fields");
        }
    }
}
