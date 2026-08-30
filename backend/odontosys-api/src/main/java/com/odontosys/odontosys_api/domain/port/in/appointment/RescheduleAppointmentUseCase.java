package com.odontosys.odontosys_api.domain.port.in.appointment;

import java.util.UUID;
import com.odontosys.odontosys_api.application.appointment.command.RescheduleAppointmentCommand;
import com.odontosys.odontosys_api.application.appointment.response.AppointmentResponse;

public interface RescheduleAppointmentUseCase {

    AppointmentResponse execute(UUID appointmentId, RescheduleAppointmentCommand command);
}
