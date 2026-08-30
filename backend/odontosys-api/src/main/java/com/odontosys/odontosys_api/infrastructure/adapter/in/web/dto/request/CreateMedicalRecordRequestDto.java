package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMedicalRecordRequestDto(
        @NotNull(message = "El ID del paciente es obligatorio")
        UUID patientId,

        @NotNull(message = "El ID del dentista es obligatorio")
        UUID dentistId,

        UUID appointmentId,

        @NotBlank(message = "El diagnóstico es obligatorio")
        String diagnosis,

        String notes,

        @Valid
        List<MedicalRecordProcedureItemRequestDto> items
) {
}
