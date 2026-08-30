package com.odontosys.odontosys_api.infrastructure.adapter.in.web.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.HolidayJpaEntity;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository.HolidayJpaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/holidays")
@Tag(name = "Días Festivos / No Laborables", description = "Endpoints para marcar y consultar días festivos y cierres del consultorio")
public class HolidayController {

    private final HolidayJpaRepository holidayRepository;

    public HolidayController(HolidayJpaRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    public record CreateHolidayRequestDto(
            @NotNull(message = "La fecha es obligatoria")
            LocalDate date,
            @NotBlank(message = "El motivo es obligatorio")
            String reason,
            boolean global,
            UUID dentistId
    ) {}

    @GetMapping
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Listar días festivos", description = "Retorna todos los días festivos registrados en la clínica")
    public ResponseEntity<List<HolidayJpaEntity>> getHolidays(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(holidayRepository.findByDateBetween(startDate, endDate));
        }
        return ResponseEntity.ok(holidayRepository.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Registrar día festivo / no laborable", description = "Permite al Administrador declarar un día festivo global o específico para un dentista")
    public ResponseEntity<HolidayJpaEntity> createHoliday(@Valid @RequestBody CreateHolidayRequestDto request) {
        HolidayJpaEntity entity = new HolidayJpaEntity(
                UUID.randomUUID(),
                request.date(),
                request.reason(),
                request.global(),
                request.dentistId()
        );
        HolidayJpaEntity saved = holidayRepository.save(entity);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar día festivo", description = "Permite eliminar un día festivo registrado")
    public ResponseEntity<Void> deleteHoliday(@PathVariable UUID id) {
        holidayRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
