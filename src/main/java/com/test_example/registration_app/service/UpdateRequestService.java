package com.test_example.registration_app.service;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.enums.EnumStatus;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static com.test_example.registration_app.enums.EnumStatus.SENT;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateRequestService {

    private final RequestRepository requestRepository;

    /**
     * Обновляет запрос на основе данных DTO.
     *
     * @param requestToUpdate Объект запроса, который требуется обновить.
     * @param requestDto      DTO, содержащий новые данные для запроса.
     * @throws IllegalArgumentException если переданы некорректные или нулевые данные.
     */
    @Transactional
    public void updateRequestFromDto(Request requestToUpdate, RequestDto requestDto) {

        if (requestToUpdate == null || requestDto == null) {
            log.error("An attempt was made to update a query with a null entity or DTO.");
            throw new IllegalArgumentException("An attempt was made to update a query with a null entity or DTO.");
        }
        if (requestDto.getStatus().equals("SENT") &&
                (!requestToUpdate.getStatus().equals(SENT) || !requestToUpdate.getText().equals(requestDto.getText()))) {
            EnumStatus status = EnumStatus.valueOf(requestDto.getStatus().toUpperCase());
            log.info("Updating a request with ID: {}", requestToUpdate.getId());
            try {
                requestToUpdate.setText(requestDto.getText());
                requestToUpdate.setStatus(status);
            } catch (IllegalArgumentException e) {
                log.error("Invalid status value '{}' provided for request ID: {}", requestDto.getStatus(), requestToUpdate.getId());
                throw new IllegalArgumentException("Invalid status value: " + requestDto.getStatus());
            }
            requestToUpdate.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            requestRepository.saveAndFlush(requestToUpdate);
        } else {
            log.error("Invalid status value '{}'", requestDto.getStatus());
            throw new IllegalArgumentException("Invalid status value: " + requestDto.getStatus());
        }
    }

    /**
     * Проверяет, имеются ли данные для изменения.
     *
     * @param requestToUpdate Объект запроса для проверки.
     * @param requestDto      DTO с данными для проверки.
     * @return true, если данные запроса совпадают с данными DTO.
     */
    public static boolean isUpdated(Request requestToUpdate, RequestDto requestDto) {
        EnumStatus status = EnumStatus.valueOf(requestDto.getStatus().toUpperCase());
        return requestDto.getText().equals(requestToUpdate.getText()) &&
                status == requestToUpdate.getStatus();
    }

    /**
     * Получает запрос по идентификатору.
     *
     * @param idRequest Идентификатор запроса.
     * @return Объект запроса.
     * @throws IllegalArgumentException если запрос не найден.
     */
    @Transactional(readOnly = true)
    public Request getRequestById(Long idRequest) {
        return requestRepository.findById(idRequest)
                .orElseThrow(() -> new IllegalArgumentException("Uncorrected Id: " + idRequest));
    }

    /**
     * Проверяет, совпадают ли имена в запросе и аутентификации.
     *
     * @param nameFromRequest Имя из запроса.
     * @param nameFromAuth    Имя из аутентификации.
     * @return true, если имена совпадают.
     */
    public boolean checkName(String nameFromRequest, String nameFromAuth) {
        return nameFromRequest.equals(nameFromAuth);
    }

    /**
     * Отправляет запрос на действие (принятие или отклонение).
     *
     * @param request Объект запроса для действия.
     * @param action  Действие, которое нужно применить ("accept" или "reject").
     * @return Обновленный объект запроса.
     * @throws IllegalArgumentException если статус запроса не позволяет выполнить действие.
     */
    @Transactional
    public Request sendAccept(Request request, String action) {
        if (request.getStatus().equals(SENT)) {
            if (action.equals("accept")) {
                request.setStatus(EnumStatus.ACCEPTED);
            } else if (action.equals("reject")) {
                request.setStatus(EnumStatus.REJECTED);
            }
            requestRepository.saveAndFlush(request);
        } else {
            log.error("Invalid status value '{}'", request.getStatus());
            throw new IllegalArgumentException("Invalid status value: " + request.getStatus());
        }
        return request;
    }
}
