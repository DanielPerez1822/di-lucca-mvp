package com.odontosys.odontosys_api.application.appointment.command;

import java.util.UUID;

public record CreateAppointmentCommand(
        UUID patientId,
        UUID dentistId,
        UUID slotId,
        String reason
) {
}
