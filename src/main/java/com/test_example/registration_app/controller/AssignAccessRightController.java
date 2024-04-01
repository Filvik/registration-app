package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RoleDto;
import com.test_example.registration_app.dtos.UserDto;
import com.test_example.registration_app.model.User;
import com.test_example.registration_app.service.RoleConverterService;
import com.test_example.registration_app.service.RoleManipulationService;
import com.test_example.registration_app.service.UserManipulationService;
import com.test_example.registration_app.service.UsersConverterService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/assign")
@RequiredArgsConstructor
public class AssignAccessRightController {

    private final UsersConverterService usersConverterService;
    private final UserManipulationService userManipulationService;
    private final RoleConverterService roleConverterService;
    private final RoleManipulationService roleManipulationService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('Administrator')")
    public String showEditForm(@RequestParam Long idUser, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userManipulationService.getUserFromDB(idUser);
            UserDto userDto = usersConverterService.convertFromUserToUserDto(user);
            model.addAttribute("userDto", userDto);
            model.addAttribute("idUser", idUser);
            return "edit_user_role_form";
        } catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error retrieving user: " + e.getMessage());
            return "redirect:/error";
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Administrator')")
    public String assignAccessRight(Long idUser, Model model, RedirectAttributes redirectAttributes, Authentication authentication,
                                    String newRole) {
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
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/error";
        }
    }
}
