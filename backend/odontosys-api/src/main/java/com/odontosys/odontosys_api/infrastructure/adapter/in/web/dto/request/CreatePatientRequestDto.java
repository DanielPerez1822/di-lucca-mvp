package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePatientRequestDto(
        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        @NotBlank(message = "El tipo de documento es obligatorio")
        String documentType,

        @NotBlank(message = "El número de documento es obligatorio")
        String documentNumber,

        String phone,

        @Email(message = "El formato de correo electrónico no es válido")
        String email,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        LocalDate dateOfBirth,

        String address
) {
}
