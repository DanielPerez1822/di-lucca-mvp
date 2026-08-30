package com.odontosys.odontosys_api.application.appointment.command;

import java.util.UUID;

public record RescheduleAppointmentCommand(
        UUID newSlotId
) {
}
