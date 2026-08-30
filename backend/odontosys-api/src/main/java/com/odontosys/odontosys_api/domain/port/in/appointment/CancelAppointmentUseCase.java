package com.odontosys.odontosys_api.domain.port.in.appointment;

import java.util.UUID;

public interface CancelAppointmentUseCase {

    void execute(UUID appointmentId, String cancelReason);
}
