package com.test_example.registration_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
public class EntranceController {

    @GetMapping("/userPage")
    @PreAuthorize("hasAuthority('User')")
    public String user() {
        return "userPage";
    }

    @GetMapping("/operatorPage")
    @PreAuthorize("hasAuthority('Operator')")
    public String operator() {
        return "operatorPage";
    }

    @GetMapping("/adminPage")
    @PreAuthorize("hasAuthority('Administrator')")
    public String admin() {
        return "adminPage";
    }
}

