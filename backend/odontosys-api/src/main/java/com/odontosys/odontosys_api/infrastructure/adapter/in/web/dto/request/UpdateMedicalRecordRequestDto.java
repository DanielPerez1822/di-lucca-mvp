package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record UpdateMedicalRecordRequestDto(
        @NotBlank(message = "El diagnóstico es obligatorio")
        String diagnosis,

        String notes,

        @Valid
        List<MedicalRecordProcedureItemRequestDto> items
) {
}
