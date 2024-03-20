package com.test_example.registration_app.model;

import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    @Schema(description = "Идентификатор")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Полное имя")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Schema(description = "Почта")
    @Column(name = "email", nullable = false)
    private String email;

    @Schema(description = "Хэш пароля")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Schema(description = "Телефон")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Schema(description = "Создано")
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Schema(description = "Обновлено")
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Schema(description = "Роли пользователя")
    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id")
    )
    private Set<Role> roles;

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
