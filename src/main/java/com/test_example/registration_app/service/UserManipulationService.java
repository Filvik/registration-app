package com.test_example.registration_app.service;

import com.test_example.registration_app.model.User;
import com.test_example.registration_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserManipulationService {

    private final UserRepository userRepository;

    public List<User> getAllUsersFromDB(){
        return userRepository.findAll();
    }
}
