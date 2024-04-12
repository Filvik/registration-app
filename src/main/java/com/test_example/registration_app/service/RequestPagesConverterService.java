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

    private final RequestConverterService requestConverterService;

    /**
     * Конвертирует страницу запросов в объект DTO для страницы запросов.
     * Использует сервис RequestConverterService для конвертации каждого отдельного запроса.
     *
     * @param requestPage Страница объектов Request, полученная из базы данных.
     * @return объект DTO для страницы запросов, содержащий информацию о содержимом страницы, текущей странице и общем количестве страниц.
     */
    public RequestPageDTO toRequestPageDTO(Page<Request> requestPage) {
        RequestPageDTO requestPageDTO = new RequestPageDTO();
        requestPageDTO.setContent(requestPage.getContent().stream().map(this::toDto).collect(Collectors.toList()));
        requestPageDTO.setCurrentPage(requestPage.getNumber());
        requestPageDTO.setTotalPages(requestPage.getTotalPages());
        return requestPageDTO;
    }

    /**
     * Вспомогательный метод для конвертации объекта Request в объект DTO RequestDto.
     * Вызывает метод fromRequestToRequestDto из сервиса RequestConverterService для выполнения конвертации.
     *
     * @param request объект Request, который нужно конвертировать.
     * @return сконвертированный объект DTO RequestDto.
     */
    private RequestDto toDto(Request request) {
        return requestConverterService.fromRequestToRequestDto(request);
    }
}
