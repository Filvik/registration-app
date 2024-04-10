package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RoleDto;
import com.test_example.registration_app.dtos.UserDto;
import com.test_example.registration_app.model.User;
import com.test_example.registration_app.service.RoleConverterService;
import com.test_example.registration_app.service.RoleManipulationService;
import com.test_example.registration_app.service.UserManipulationService;
import com.test_example.registration_app.service.UsersConverterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/assign")
@RequiredArgsConstructor
@Tag(name = "AssignAccessRightController", description = "Контроллер для назначения прав доступа пользователям")
public class AssignAccessRightController {

    private final UsersConverterService usersConverterService;
    private final UserManipulationService userManipulationService;
    private final RoleConverterService roleConverterService;
    private final RoleManipulationService roleManipulationService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('Administrator')")
    @Operation(summary = "Показать форму назначения прав доступа",
            description = "Показывает форму для назначения прав доступа пользователям. Доступен только администраторам.")
    public String showEditForm(@Parameter(description = "ID пользователя") @RequestParam Long idUser,
                               Model model) {
        try {
            User user = userManipulationService.getUserFromDB(idUser);
            UserDto userDto = usersConverterService.convertFromUserToUserDto(user);
            model.addAttribute("userDto", userDto);
            model.addAttribute("idUser", idUser);
            return "edit_user_role_form";
        } catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            model.addAttribute("errorMessage", "Error retrieving user: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Administrator')")
    @Operation(summary = "Назначить права доступа",
            description = "Назначает новые права доступа пользователю. Доступен только администраторам.")
    public String assignAccessRight(@Parameter(description = "ID пользователя") @RequestParam Long idUser,
                                    Model model,
                                    Authentication authentication,
                                    @Parameter(description = "Новая роль пользователя ('User', 'Operator' или 'Administrator')")
                                    @RequestParam String newRole) {
        try {
            User user = userManipulationService.addRoleForUser(idUser, newRole);
            Long userId = user.getId();
            UserDto userDto = usersConverterService.convertFromUserToUserDto(userManipulationService.getUserFromDB(userId));
            Set<RoleDto> roleDtos = roleConverterService.convertFromRoleToRoleDto(roleManipulationService.getRoleFromDB(userId));
            model.addAttribute("userDto", userDto);
            model.addAttribute("userName", authentication.getName());
            model.addAttribute("roleDtos", roleDtos);
            return "added_new_role";
        } catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "error";
        }
    }
}
