package com.odontosys.odontosys_api.domain.port.in.appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.appointment.response.AppointmentResponse;
import com.odontosys.odontosys_api.domain.model.AppointmentStatus;

public interface ListAppointmentsUseCase {

    List<AppointmentResponse> execute(UUID dentistId, UUID patientId, LocalDate startDate, LocalDate endDate, AppointmentStatus status);
}
