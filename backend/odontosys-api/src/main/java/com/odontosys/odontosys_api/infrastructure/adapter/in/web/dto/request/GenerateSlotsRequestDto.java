package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import java.time.LocalDate;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public record GenerateSlotsRequestDto(
        UUID dentistId,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate startDate,

        @NotNull(message = "La fecha de fin es obligatoria")
        LocalDate endDate
) {
}
