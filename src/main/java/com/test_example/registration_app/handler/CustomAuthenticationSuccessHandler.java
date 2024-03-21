package com.test_example.registration_app.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("User"))) {
            response.sendRedirect(request.getContextPath() + "/userPage");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Operator"))) {
            response.sendRedirect(request.getContextPath() + "/operatorPage");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Administrator"))) {
            response.sendRedirect(request.getContextPath() + "/adminPage");
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
