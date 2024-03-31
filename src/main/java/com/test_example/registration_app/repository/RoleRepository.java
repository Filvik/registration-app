package com.test_example.registration_app.repository;

import com.test_example.registration_app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String name);
    @Query("SELECT r FROM Role r JOIN r.users u WHERE u.id = :userId")
    Set<Role> findByUserId(@Param("userId") Long userId);
}
