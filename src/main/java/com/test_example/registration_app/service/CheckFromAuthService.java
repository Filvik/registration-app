package com.test_example.registration_app.service;

import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.model.User;
import com.test_example.registration_app.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckFromAuthService {

    private final RequestRepository requestRepository;

    public boolean checkRole(Authentication authentication, String roleName) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().anyMatch(a -> a.getAuthority().equals(roleName));
    }

    public boolean checkRequest(Long idRequest, String authenticationName) {
        return requestRepository.findById(idRequest)
                .map(Request::getUser)
                .map(User::getFullName)
                .filter(authenticationName::equals)
                .isPresent();
    }
}
