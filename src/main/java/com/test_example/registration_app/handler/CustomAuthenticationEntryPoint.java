package com.test_example.registration_app.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String errorMessage = "Unauthorized: Authentication is required to access this resource.";
        String encodedMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);

        String redirectUrl = UriComponentsBuilder
                .fromPath("/noAuthorizationError")
                .queryParam("error", encodedMessage)
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}

//@Component
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//                         AuthenticationException authException) throws IOException {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setContentType("text/plain");
//        response.getWriter().write("Unauthorized: Authentication is required to access this resource.");
//    }
//}
