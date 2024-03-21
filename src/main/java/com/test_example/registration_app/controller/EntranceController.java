package com.test_example.registration_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
public class EntranceController {

    @GetMapping("/universalFormPage")
    @PreAuthorize("hasAnyAuthority('User', 'Operator', 'Administrator')")
    public String admin(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("authorities", authentication.getAuthorities());
        return "universalFormPage";
    }
}
