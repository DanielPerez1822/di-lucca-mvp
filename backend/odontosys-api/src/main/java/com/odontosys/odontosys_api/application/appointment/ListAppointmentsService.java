package com.odontosys.odontosys_api.application.appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.appointment.response.AppointmentResponse;
import com.odontosys.odontosys_api.domain.model.AppointmentStatus;
import com.odontosys.odontosys_api.domain.port.in.appointment.ListAppointmentsUseCase;
import com.odontosys.odontosys_api.domain.port.out.AppointmentRepositoryPort;

public class ListAppointmentsService implements ListAppointmentsUseCase {

    private final AppointmentRepositoryPort appointmentRepository;

    public ListAppointmentsService(AppointmentRepositoryPort appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<AppointmentResponse> execute(UUID dentistId, UUID patientId, LocalDate startDate, LocalDate endDate, AppointmentStatus status) {
        return appointmentRepository.findByFilters(dentistId, patientId, startDate, endDate, status).stream()
                .map(AppointmentResponse::fromDomain)
                .toList();
    }
}
