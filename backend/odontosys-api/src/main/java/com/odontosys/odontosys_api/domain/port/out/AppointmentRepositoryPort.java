package com.odontosys.odontosys_api.domain.port.out;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.Appointment;
import com.odontosys.odontosys_api.domain.model.AppointmentStatus;

public interface AppointmentRepositoryPort {

    Appointment save(Appointment appointment);

    Optional<Appointment> findById(UUID id);

    List<Appointment> findByFilters(UUID dentistId, UUID patientId, LocalDate startDate, LocalDate endDate, AppointmentStatus status);
}
