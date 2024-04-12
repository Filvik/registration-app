package com.test_example.registration_app.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID uuid;

    @Schema(required = true, description = "Имя пользователя")
    private String userName;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String phoneNumber;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String email;

    @Schema(required = true, description = "Статус: DRAFT, SENT")
    private String status;

    @Schema(required = true, description = "Сообщение")
    private String text;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp createdAt;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Timestamp updatedAt;
}
