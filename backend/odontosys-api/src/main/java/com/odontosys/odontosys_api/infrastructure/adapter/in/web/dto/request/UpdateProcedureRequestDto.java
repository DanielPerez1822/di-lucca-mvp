package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateProcedureRequestDto(
        @NotBlank(message = "El nombre del procedimiento es obligatorio")
        String name,

        String description,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
        BigDecimal price,

        @Min(value = 1, message = "La duración debe ser mayor a 0 minutos")
        Integer duration
) {
}
