package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdatePatientRequestDto(
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

        LocalDate dateOfBirth,

        String address
) {
}
