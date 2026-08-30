package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import com.odontosys.odontosys_api.application.appointment.response.AppointmentResponse;
import com.odontosys.odontosys_api.domain.model.AppointmentStatus;

public record AppointmentResponseDto(
        UUID id,
        UUID patientId,
        UUID dentistId,
        UUID slotId,
        LocalDate appointmentDate,
        LocalTime startTime,
        LocalTime endTime,
        String reason,
        AppointmentStatus status,
        String notes,
        Instant createdAt,
        Instant updatedAt
) {
    public static AppointmentResponseDto fromApplication(AppointmentResponse response) {
        return new AppointmentResponseDto(
                response.id(),
                response.patientId(),
                response.dentistId(),
                response.slotId(),
                response.appointmentDate(),
                response.startTime(),
                response.endTime(),
                response.reason(),
                response.status(),
                response.notes(),
                response.createdAt(),
                response.updatedAt()
        );
    }
}
