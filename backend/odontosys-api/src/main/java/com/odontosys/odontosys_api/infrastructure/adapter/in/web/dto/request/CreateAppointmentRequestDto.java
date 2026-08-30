package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public record CreateAppointmentRequestDto(
        @NotNull(message = "El ID del paciente es obligatorio")
        UUID patientId,

        @NotNull(message = "El ID del dentista es obligatorio")
        UUID dentistId,

        @NotNull(message = "El ID del slot de disponibilidad es obligatorio")
        UUID slotId,

        String reason
) {
}
