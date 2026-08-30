package com.odontosys.odontosys_api.application.patient.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.Patient;

public record PatientResponse(
        UUID id,
        String firstName,
        String lastName,
        String documentType,
        String documentNumber,
        String phone,
        String email,
        LocalDate dateOfBirth,
        String address,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
    public static PatientResponse fromDomain(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDocumentType(),
                patient.getDocumentNumber(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getDateOfBirth(),
                patient.getAddress(),
                patient.isActive(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
}
