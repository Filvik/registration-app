package com.test_example.registration_app.service;

import com.test_example.registration_app.enums.EnumStatus;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.model.User;
import com.test_example.registration_app.repository.RequestRepository;
import com.test_example.registration_app.repository.UserRepository;
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

    public Request createRequest(RequestDto requestDto) {

        RequestDtoConverterService.validateRequestDto(requestDto);

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
    }
}
