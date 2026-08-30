package com.odontosys.odontosys_api.application.appointment;

import java.util.UUID;
import com.odontosys.odontosys_api.application.appointment.response.AppointmentResponse;
import com.odontosys.odontosys_api.domain.exception.AppointmentNotFoundException;
import com.odontosys.odontosys_api.domain.port.in.appointment.GetAppointmentByIdUseCase;
import com.odontosys.odontosys_api.domain.port.out.AppointmentRepositoryPort;

public class GetAppointmentByIdService implements GetAppointmentByIdUseCase {

    private final AppointmentRepositoryPort appointmentRepository;

    public GetAppointmentByIdService(AppointmentRepositoryPort appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public AppointmentResponse execute(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .map(AppointmentResponse::fromDomain)
                .orElseThrow(() -> new AppointmentNotFoundException("Cita no encontrada con ID: " + appointmentId));
    }
}
