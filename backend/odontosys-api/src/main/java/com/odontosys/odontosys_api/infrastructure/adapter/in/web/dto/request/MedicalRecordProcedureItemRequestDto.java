package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import java.math.BigDecimal;
import java.util.UUID;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record MedicalRecordProcedureItemRequestDto(
        @NotNull(message = "El ID del procedimiento es obligatorio")
        UUID procedureId,

        @DecimalMin(value = "0.0", message = "El precio aplicado no puede ser negativo")
        BigDecimal appliedPrice,

        Integer toothNumber,

        String notes
) {
}
