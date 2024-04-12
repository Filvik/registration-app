package com.test_example.registration_app.service;

import com.test_example.registration_app.dtos.RegistrationUserDto;
import com.test_example.registration_app.dtos.RoleDto;
import com.test_example.registration_app.exceptions.RegistrationUserDtoException;
import com.test_example.registration_app.model.Role;
import com.test_example.registration_app.model.User;
import com.test_example.registration_app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleService roleService, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Загружает детали пользователя по его имени пользователя для аутентификации.
     * @param username имя пользователя, по которому осуществляется поиск в базе данных.
     * @return UserDetails содержащий информацию о пользователе, включая имя, пароль и роли.
     * @throws UsernameNotFoundException если пользователь не найден в базе данных.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByFullName(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found", username)));
        user.setRoles(roleService.getUserRolesFromBD(user.getId()));
        return new org.springframework.security.core.userdetails.User(
                user.getFullName(),
                user.getPasswordHash(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList())
        );
    }

    /**
     * Создает нового пользователя на основе предоставленного DTO.
     * @param registrationUserDto DTO содержащее данные для регистрации пользователя, включая имя, пароль, email и роли.
     * @return User сохраненный и обновленный пользователь.
     * @throws RegistrationUserDtoException если данные DTO не проходят валидацию.
     */
    @Transactional
    public User createNewUser(RegistrationUserDto registrationUserDto) {
        if (validateRegistrationUserDto(registrationUserDto)){
            User user = new User();
            user.setFullName(registrationUserDto.getUsername());
            user.setEmail(registrationUserDto.getEmail());
            user.setPhoneNumber(registrationUserDto.getPhoneNumber());
            user.setPasswordHash(passwordEncoder.encode(registrationUserDto.getPassword()));
            List<String> roleNames = registrationUserDto.getRoles().stream()
                    .map(RoleDto::getRoleName)
                    .collect(Collectors.toList());
            Set<Role> userRoles = roleService.getUserRolesForAddInBD(roleNames);
            user.setRoles(userRoles);
            return userRepository.saveAndFlush(user);
        }
        else {
            log.error("Validation failed for RegistrationUserDto");
            throw new RegistrationUserDtoException("Validation failed for RegistrationUserDto");
        }
    }

    /**
     * Валидирует данные регистрации пользователя.
     * @param registrationUserDto DTO для регистрации пользователя.
     * @return boolean true, если данные корректны.
     */
    public boolean validateRegistrationUserDto(RegistrationUserDto registrationUserDto) {
        if (registrationUserDto == null) {
            log.error("RegistrationUserDto is null");
            return false;
        }
        if (!StringUtils.hasText(registrationUserDto.getUsername())) {
            log.error("Username is required");
            return false;
        }
        if (!StringUtils.hasText(registrationUserDto.getPassword())) {
            log.error("Password is required");
            return false;
        }
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            log.error("Passwords do not match");
            return false;
        }
        if (!StringUtils.hasText(registrationUserDto.getEmail())) {
            log.error("Email is required");
            return false;
        }
        if (registrationUserDto.getRoles() == null || registrationUserDto.getRoles().isEmpty()) {
            log.error("At least one role is required");
            return false;
        }
        return true;
    }
}
