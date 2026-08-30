package com.odontosys.odontosys_api.domain.port.out;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.AvailabilitySlot;
import com.odontosys.odontosys_api.domain.model.SlotStatus;

public interface AvailabilitySlotRepositoryPort {

    AvailabilitySlot save(AvailabilitySlot slot);

    List<AvailabilitySlot> saveAll(List<AvailabilitySlot> slots);

    Optional<AvailabilitySlot> findById(UUID id);

    List<AvailabilitySlot> findByDentistIdAndDateBetween(UUID dentistId, LocalDate startDate, LocalDate endDate);

    List<AvailabilitySlot> findByDentistIdAndDateBetweenAndStatus(UUID dentistId, LocalDate startDate, LocalDate endDate, SlotStatus status);

    boolean existsByDentistIdAndDateAndStartTime(UUID dentistId, LocalDate date, java.time.LocalTime startTime);
}
