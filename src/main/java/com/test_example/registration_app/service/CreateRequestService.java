package com.test_example.registration_app.service;

import com.test_example.registration_app.enums.EnumStatus;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.model.User;
import com.test_example.registration_app.repository.RequestRepository;
import com.test_example.registration_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateRequestService {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    /**
     * Создает новый запрос на основе данных DTO.
     * Метод проверяет статус запроса и существование пользователя в системе перед сохранением запроса.
     *
     * @param requestDto объект DTO, содержащий данные запроса.
     * @return сохраненный объект запроса.
     * @throws IllegalArgumentException если пользователь не найден или статус запроса не позволяет его создать.
     */
    @Transactional
    public Request createRequest(RequestDto requestDto) {
        if (requestDto.getStatus().equals("DRAFT") ||
                requestDto.getStatus().equals("SENT")) {
            RequestConverterService.validateRequestDto(requestDto);

            Optional<User> userOptional = userRepository.findByFullName(requestDto.getUserName());
            if (userOptional.isPresent()) {

                Request request = new Request();
                request.setUser(userOptional.get());
                request.setId(userOptional.get().getId());
                request.setStatus(Enum.valueOf(EnumStatus.class, requestDto.getStatus().toUpperCase()));
                request.setText(requestDto.getText());

                return requestRepository.saveAndFlush(request);
            } else {
                log.error("User with UserName " + requestDto.getUserName() + " not found");
                throw new IllegalArgumentException("User with UserName " + requestDto.getUserName() + " not found");
            }
        } else {
            log.error("User with UserName " + requestDto.getUserName() + " does not have rights for this status");
            throw new IllegalArgumentException("User with UserName " + requestDto.getUserName() + "  does not have rights for this status");
        }
    }
}
