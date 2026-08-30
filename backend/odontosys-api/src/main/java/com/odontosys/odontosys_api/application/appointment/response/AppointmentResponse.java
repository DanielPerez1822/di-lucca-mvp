package com.odontosys.odontosys_api.application.appointment.response;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.Appointment;
import com.odontosys.odontosys_api.domain.model.AppointmentStatus;

public record AppointmentResponse(
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
    public static AppointmentResponse fromDomain(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatientId(),
                appointment.getDentistId(),
                appointment.getSlotId(),
                appointment.getAppointmentDate(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getReason(),
                appointment.getStatus(),
                appointment.getNotes(),
                appointment.getCreatedAt(),
                appointment.getUpdatedAt()
        );
    }
}
