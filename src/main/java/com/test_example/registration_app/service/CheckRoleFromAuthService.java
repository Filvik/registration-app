package com.test_example.registration_app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
public class CheckRoleFromAuthService {

    public boolean checkRole(Authentication authentication, String roleName) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().anyMatch(a -> a.getAuthority().equals(roleName));
    }
}
