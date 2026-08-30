package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.odontosys.odontosys_api.domain.model.SlotStatus;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.AvailabilitySlotJpaEntity;

public interface SpringDataAvailabilitySlotRepository extends JpaRepository<AvailabilitySlotJpaEntity, UUID> {

    List<AvailabilitySlotJpaEntity> findByDentistIdAndDateBetweenOrderByDateAscStartTimeAsc(UUID dentistId, LocalDate startDate, LocalDate endDate);

    List<AvailabilitySlotJpaEntity> findByDentistIdAndDateBetweenAndStatusOrderByDateAscStartTimeAsc(UUID dentistId, LocalDate startDate, LocalDate endDate, SlotStatus status);

    boolean existsByDentistIdAndDateAndStartTime(UUID dentistId, LocalDate date, LocalTime startTime);
}
