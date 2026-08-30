package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.odontosys.odontosys_api.domain.model.AppointmentStatus;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.AppointmentJpaEntity;

public interface SpringDataAppointmentRepository extends JpaRepository<AppointmentJpaEntity, UUID> {

    @Query("SELECT a FROM AppointmentJpaEntity a WHERE " +
           "(:dentistId IS NULL OR a.dentistId = :dentistId) AND " +
           "(:patientId IS NULL OR a.patientId = :patientId) AND " +
           "(:startDate IS NULL OR a.appointmentDate >= :startDate) AND " +
           "(:endDate IS NULL OR a.appointmentDate <= :endDate) AND " +
           "(:status IS NULL OR a.status = :status) " +
           "ORDER BY a.appointmentDate ASC, a.startTime ASC")
    List<AppointmentJpaEntity> findByFilters(
            @Param("dentistId") UUID dentistId,
            @Param("patientId") UUID patientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") AppointmentStatus status
    );
}
