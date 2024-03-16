package com.test_example.registration_app.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Schema(description = "Идентификатор")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Название роли")
    @Column(name = "role_name")
    private String roleName;

    @Schema(description = "Пользователи с этой ролью")
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

}
