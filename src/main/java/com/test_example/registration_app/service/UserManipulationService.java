package com.test_example.registration_app.service;

import com.test_example.registration_app.model.Role;
import com.test_example.registration_app.model.User;
import com.test_example.registration_app.repository.RoleRepository;
import com.test_example.registration_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserManipulationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /**
     * Возвращает список всех пользователей из базы данных.
     *
     * @return Список пользователей.
     */
    public List<User> getAllUsersFromDB() {
        return userRepository.findAll();
    }

    /**
     * Получает пользователя по его идентификатору.
     * Если пользователь не найден, выбрасывается исключение UsernameNotFoundException.
     *
     * @param id Идентификатор пользователя.
     * @return Пользователь.
     * @throws UsernameNotFoundException если пользователь с указанным ID не найден.
     */
    public User getUserFromDB(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("This user doesn't exist"));
    }

    /**
     * Добавляет новую роль пользователю.
     * Если роль уже присвоена пользователю или такой роли нет в базе данных, выбрасывается исключение.
     *
     * @param id Идентификатор пользователя.
     * @param newRole Название роли, которая будет добавлена пользователю.
     * @return Обновлённый пользователь.
     * @throws IllegalArgumentException если роль уже присвоена пользователю или роль не существует.
     */
    @Transactional
    public User addRoleForUser(Long id, String newRole) {
        Optional<Role> checkRole = roleRepository.findByRoleName(newRole);
        if (checkRole.isPresent()) {
            User user = getUserFromDB(id);
            Set<Role> roles = user.getRoles();
            if (roles.contains(checkRole.get())) {
                log.warn("The added role about the user already exists");
                throw new IllegalArgumentException("The added role about the user already exists");
            } else {
                roles.add(checkRole.get());
                user.setRoles(roles);
                return userRepository.saveAndFlush(user);
            }
        } else {
            log.warn("That role doesn't exist in DataBase");
            throw new IllegalArgumentException("That role doesn't exist in DataBase");
        }
    }
}
