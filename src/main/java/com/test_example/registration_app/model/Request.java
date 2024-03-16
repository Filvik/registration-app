package com.test_example.registration_app.model;

import com.test_example.registration_app.enums.EnumStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "requests")
@Data
public class Request {

    @Schema(description = "Идентификатор")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "UUID")
    @Column(updatable = false, unique = true, nullable = false)
    private UUID uuid = UUID.randomUUID();

    @Schema(description = "Пользователь")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Schema(description = "Статус")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnumStatus status;

    @Schema(description = "Текст")
    @Column(name = "text")
    private String text;

    @Schema(description = "Создано")
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Schema(description = "Обновлено")
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

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