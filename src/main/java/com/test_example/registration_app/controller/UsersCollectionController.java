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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/getUsers")
@RequiredArgsConstructor
@Tag(name = "UsersCollectionController", description = "Контроллер для работы со списком пользователей")
public class UsersCollectionController {

    private final UsersConverterService usersConverterService;
    private final UserManipulationService userManipulationService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('Administrator')")
    @Operation(summary = "Получить всех пользователей",
            description = "Возвращает список всех пользователей из базы данных. Доступен только администраторам.")
    public String getAllUsersFromBD(Model model) {
        try {
            List<UserDto> userDtos = usersConverterService.convertFromUserToUserDto(userManipulationService.getAllUsersFromDB());
            model.addAttribute("userDtos", userDtos);
            return "get_all_users";
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "error";
        }
    }
}
