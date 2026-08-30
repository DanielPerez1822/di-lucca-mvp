package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequestDto(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato de email no es válido")
        String email,

        @NotBlank(message = "El código de verificación es obligatorio")
        String code,

        @NotBlank(message = "La nueva contraseña es obligatoria")
        @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
        String newPassword
) {
}
