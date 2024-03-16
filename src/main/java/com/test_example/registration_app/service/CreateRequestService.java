package com.test_example.registration_app.service;

import com.test_example.registration_app.enums.EnumStatus;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.model.RequestDto;
import com.test_example.registration_app.model.User;
import com.test_example.registration_app.repository.RequestRepository;
import com.test_example.registration_app.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class CreateRequestService {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    public Request createRequest(RequestDto requestDto) {

        RequestDtoConverterService.validateRequestDto(requestDto);

        Optional<User> userOptional = userRepository.findById(requestDto.getUserId());
        if (userOptional.isPresent()) {

            Request request = new Request();
            request.setUser(userOptional.get());
            request.setStatus(Enum.valueOf(EnumStatus.class, requestDto.getStatus().toUpperCase()));
            request.setText(requestDto.getText());

            return requestRepository.saveAndFlush(request);
        } else {
            log.error("User with ID " + requestDto.getUserId() + " not found");
            throw new IllegalArgumentException("User with ID " + requestDto.getUserId() + " not found");
        }
    }
}
