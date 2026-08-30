package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import com.odontosys.odontosys_api.application.patient.response.PatientResponse;

public record PatientResponseDto(
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
    public static PatientResponseDto fromApplication(PatientResponse response) {
        return new PatientResponseDto(
                response.id(),
                response.firstName(),
                response.lastName(),
                response.documentType(),
                response.documentNumber(),
                response.phone(),
                response.email(),
                response.dateOfBirth(),
                response.address(),
                response.active(),
                response.createdAt(),
                response.updatedAt()
        );
    }
}
