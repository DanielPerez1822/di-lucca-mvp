package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.HolidayJpaEntity;

@Repository
public interface HolidayJpaRepository extends JpaRepository<HolidayJpaEntity, UUID> {

    List<HolidayJpaEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT h FROM HolidayJpaEntity h WHERE h.date = :date AND (h.global = true OR h.dentistId = :dentistId)")
    List<HolidayJpaEntity> findHolidaysForDentistOnDate(@Param("date") LocalDate date, @Param("dentistId") UUID dentistId);

    boolean existsByDateAndGlobalTrue(LocalDate date);
}
