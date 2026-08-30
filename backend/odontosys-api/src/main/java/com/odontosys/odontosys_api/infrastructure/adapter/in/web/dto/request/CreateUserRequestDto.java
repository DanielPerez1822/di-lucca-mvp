package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import java.util.Set;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import com.odontosys.odontosys_api.domain.model.Role;

public record CreateUserRequestDto(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato de email no es válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        String phone,

        String documentNumber,

        @NotEmpty(message = "Debe especificar al menos un rol")
        Set<Role> roles
) {
}
