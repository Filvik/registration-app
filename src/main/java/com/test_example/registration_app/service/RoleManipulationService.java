package com.test_example.registration_app.service;

import com.test_example.registration_app.exceptions.UserNotFoundException;
import com.test_example.registration_app.model.Role;
import com.test_example.registration_app.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleManipulationService {

    private final RoleRepository roleRepository;

    /**
     * Получает набор ролей пользователя по его идентификатору.
     * Метод использует репозиторий для поиска ролей, связанных с определенным пользователем.
     * Выбрасывает исключение, если роли для указанного пользователя не найдены.
     *
     * @param idUser Идентификатор пользователя, для которого требуется получить роли.
     * @return Набор ролей пользователя.
     * @throws UserNotFoundException если роли для пользователя не найдены.
     */
    @Transactional(readOnly = true)
    public Set<Role> getRoleFromDB(Long idUser) {
        Set<Role> roles = roleRepository.findByUserId(idUser);
        if (roles.isEmpty()) {
            log.error("No roles found for user with ID: {}", idUser);
            throw new UserNotFoundException("No roles found for user with ID: " + idUser);
        }
        return roles;
    }
}
