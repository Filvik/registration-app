package com.test_example.registration_app.service;

import com.test_example.registration_app.dtos.JwtRequest;
import com.test_example.registration_app.dtos.JwtResponse;
import com.test_example.registration_app.dtos.RegistrationUserDto;
import com.test_example.registration_app.dtos.AddNewUserDto;
import com.test_example.registration_app.dtos.ApplicationErrorDto;
import com.test_example.registration_app.model.User;
import com.test_example.registration_app.repository.UserRepository;
import com.test_example.registration_app.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    /**
     * Аутентификация пользователя и генерация JWT.
     *
     * @param authRequest объект, содержащий имя пользователя и пароль.
     * @return ResponseEntity с JWT в случае успеха или с ошибкой, если аутентификация не удалась.
     * @throws BadCredentialsException если имя пользователя или пароль неверны.
     */
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ApplicationErrorDto(HttpStatus.UNAUTHORIZED.value(), "Incorrect login or password."), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    /**
     * Регистрация нового пользователя в системе.
     *
     * @param registrationUserDto объект, содержащий информацию о новом пользователе, включая имя и пароль.
     * @return ResponseEntity с информацией о созданном пользователе или с ошибкой, если регистрация не удалась.
     */
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return new ResponseEntity<>(new ApplicationErrorDto(HttpStatus.BAD_REQUEST.value(), "Password mismatch."), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findByFullName(registrationUserDto.getUsername()).isPresent()) {
            return new ResponseEntity<>(new ApplicationErrorDto(HttpStatus.BAD_REQUEST.value(), "A user with the specified name already exists."), HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(new AddNewUserDto("New user added successfully.", user.getFullName()));
    }
}
