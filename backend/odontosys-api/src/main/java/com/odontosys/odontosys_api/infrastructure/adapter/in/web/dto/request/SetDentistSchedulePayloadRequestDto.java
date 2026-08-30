package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public record SetDentistSchedulePayloadRequestDto(
        @NotNull(message = "El ID del dentista es obligatorio")
        UUID dentistId,

        @NotNull(message = "La lista de días es obligatoria")
        List<SetDentistScheduleItemRequestDto> days
) {
}
