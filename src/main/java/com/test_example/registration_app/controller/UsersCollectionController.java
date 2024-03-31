package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.UserDto;
import com.test_example.registration_app.service.UserManipulationService;
import com.test_example.registration_app.service.UsersConverterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/getUsers")
@RequiredArgsConstructor
public class UsersCollectionController {

    private final UsersConverterService usersConverterService;
    private final UserManipulationService userManipulationService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('Administrator')")
    public String getAllUsersFromBD(Model model) {
        try {
            List<UserDto> userDtos = usersConverterService.convertFromUserToUserDto(userManipulationService.getAllUsersFromDB());
            model.addAttribute("usersDtos", userDtos);
            return "get_all_users";
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "error";
        }
    }
}
