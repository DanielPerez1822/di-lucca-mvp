package com.odontosys.odontosys_api.domain.port.in.appointment;

import com.odontosys.odontosys_api.application.appointment.command.CreateAppointmentCommand;
import com.odontosys.odontosys_api.application.appointment.response.AppointmentResponse;

public interface CreateAppointmentUseCase {

    AppointmentResponse execute(CreateAppointmentCommand command);
}
