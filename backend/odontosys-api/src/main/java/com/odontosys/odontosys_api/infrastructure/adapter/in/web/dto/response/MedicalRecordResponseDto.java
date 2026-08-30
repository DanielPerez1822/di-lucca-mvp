package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.medicalrecord.response.MedicalRecordResponse;

public record MedicalRecordResponseDto(
        UUID id,
        UUID patientId,
        UUID dentistId,
        UUID appointmentId,
        String diagnosis,
        String notes,
        List<MedicalRecordProcedureItemResponseDto> items,
        BigDecimal totalAmount,
        Instant createdAt,
        Instant updatedAt
) {
    public static MedicalRecordResponseDto fromApplication(MedicalRecordResponse response) {
        List<MedicalRecordProcedureItemResponseDto> itemDtos = response.items() != null ?
                response.items().stream().map(MedicalRecordProcedureItemResponseDto::fromApplication).toList() : List.of();

        return new MedicalRecordResponseDto(
                response.id(),
                response.patientId(),
                response.dentistId(),
                response.appointmentId(),
                response.diagnosis(),
                response.notes(),
                itemDtos,
                response.totalAmount(),
                response.createdAt(),
                response.updatedAt()
        );
    }
}
