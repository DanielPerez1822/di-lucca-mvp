package com.odontosys.odontosys_api.application.medicalrecord.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.MedicalRecord;

public record MedicalRecordResponse(
        UUID id,
        UUID patientId,
        UUID dentistId,
        UUID appointmentId,
        String diagnosis,
        String notes,
        List<MedicalRecordProcedureItemResponse> items,
        BigDecimal totalAmount,
        Instant createdAt,
        Instant updatedAt
) {
    public static MedicalRecordResponse fromDomain(MedicalRecord record) {
        List<MedicalRecordProcedureItemResponse> itemResponses = record.getItems().stream()
                .map(MedicalRecordProcedureItemResponse::fromDomain)
                .toList();

        return new MedicalRecordResponse(
                record.getId(),
                record.getPatientId(),
                record.getDentistId(),
                record.getAppointmentId(),
                record.getDiagnosis(),
                record.getNotes(),
                itemResponses,
                record.getTotalAmount(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }
}
