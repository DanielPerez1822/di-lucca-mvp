package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequestDto(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato de email no es válido")
        String email
) {
}
