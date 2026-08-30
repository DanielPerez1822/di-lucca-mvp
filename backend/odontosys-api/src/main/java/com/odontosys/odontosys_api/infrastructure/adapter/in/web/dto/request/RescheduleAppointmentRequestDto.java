package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public record RescheduleAppointmentRequestDto(
        @NotNull(message = "El ID del nuevo slot es obligatorio")
        UUID newSlotId
) {
}
