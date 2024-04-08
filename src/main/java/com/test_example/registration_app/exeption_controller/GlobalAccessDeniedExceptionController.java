package com.test_example.registration_app.exeption_controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalAccessDeniedExceptionController {

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex, Model model) {
        model.addAttribute("errorMessage", "You don't have permission to access this page.");
        return "error";
    }
}
